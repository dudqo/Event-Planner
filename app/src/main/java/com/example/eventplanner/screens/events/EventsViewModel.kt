package com.example.eventplanner.screens.events

import android.annotation.SuppressLint
import androidx.compose.runtime.*
import androidx.core.content.ContentProviderCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eventplanner.AutocompleteResult
import com.example.eventplanner.MapEvent
import com.example.eventplanner.domain.model.Event
import com.example.eventplanner.domain.repository.EventRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventsViewModel @Inject constructor(
    private val repository: EventRepository
): ViewModel() {

    var title by mutableStateOf("")
    var lat by mutableStateOf(0.00)
    var lng by mutableStateOf(0.00)
    var address by mutableStateOf("")
    var desc by mutableStateOf("")
    var time by mutableStateOf("")
    var isPrivate by mutableStateOf(false)
    var useCurrLocation by mutableStateOf(false)
    private var currentEventId: Int? = null
    var state by mutableStateOf(EventState())
    private var job: Job? = null

    val locationAutofill = mutableStateListOf<AutocompleteResult>()
    lateinit var placesClient: PlacesClient
    var currMapLatLong by mutableStateOf(LatLng(0.0, 0.0))
    var deviceLocation by mutableStateOf(LatLng(0.0, 0.0))


    init {
        viewModelScope.launch {
            repository.getEvents().collectLatest { events ->
                state = state.copy(
                    events = events
                )
            }
        }
    }
    fun onEvent(event: EventsEvent) {
        when(event) {
            is EventsEvent.OnTitleChange -> {
                title = event.newTitle
            }
            is EventsEvent.OnAddressChange -> {
                address = event.newAddress
            }
            is EventsEvent.OnDescChange -> {
                desc = event.newDesc
            }
            is EventsEvent.OnUseCurrLocationChange -> {
                useCurrLocation = event.newUseCurrLocation
            }
            is EventsEvent.OnCreateEventClick -> {
                viewModelScope.launch {
                    repository.insertEvent(
                        Event(currentEventId, title, lat, lng, address, desc, time, isPrivate)
                    )
                }
            }
            is EventsEvent.OnDeleteEventClick -> {
                viewModelScope.launch {
                    repository.deleteEvent(event.event)
                }
            }
        }
    }

    fun searchPlaces(query: String) {
        job?.cancel()
        locationAutofill.clear()
        job = viewModelScope.launch {
            val request = FindAutocompletePredictionsRequest
                .builder()
                .setQuery(query)
                .build()
            placesClient
                .findAutocompletePredictions(request)
                .addOnSuccessListener { response ->
                    locationAutofill += response.autocompletePredictions.map {
                        AutocompleteResult(
                            it.getFullText(null).toString(),
                            it.placeId
                        )
                    }
                }
                .addOnFailureListener {
                    it.printStackTrace()
                    println(it.cause)
                    println(it.message)
                }
        }
    }

    @SuppressLint("MissingPermission")
    fun getDeviceLocation(
        fusedLocationProviderClient: FusedLocationProviderClient
    ) {
        try {
            val locationResult = fusedLocationProviderClient.lastLocation
            locationResult.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    state.lastKnownLocation = task.result
                }
            }
        } catch (e: SecurityException) {
            // Show error
        }
    }

    fun getCoordinates(result: AutocompleteResult) {
        val placeFields = listOf(Place.Field.LAT_LNG)
        val request = FetchPlaceRequest.newInstance(result.placeId, placeFields)
        placesClient.fetchPlace(request).addOnSuccessListener {
            if (it != null) {
                currMapLatLong = it.place.latLng!!
                lat = currMapLatLong.latitude
                lng = currMapLatLong.longitude
            }
        }.addOnFailureListener {
            it.printStackTrace()
        }
    }
}