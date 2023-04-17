package com.example.eventplanner.screens.events

import android.annotation.SuppressLint
import androidx.compose.runtime.*
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eventplanner.AutocompleteResult
import com.example.eventplanner.domain.model.Event
import com.example.eventplanner.domain.repository.EventRepository
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
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

@OptIn(ExperimentalPermissionsApi::class)
@HiltViewModel
class EventsViewModel @Inject constructor(
    private val repository: EventRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    var title by mutableStateOf("")
    var lat by mutableStateOf(0.00)
    var lng by mutableStateOf(0.00)
    var address by mutableStateOf("")
    var desc by mutableStateOf("")
    var time by mutableStateOf("")
    var isPrivate by mutableStateOf(false)
    var useCurrLocation by mutableStateOf(false)
    var currentEventId: Int? = null
    var state by mutableStateOf(EventState())
    private var job: Job? = null
    lateinit var currEvent: Event
    lateinit var fusedLocationClient: FusedLocationProviderClient

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
        savedStateHandle.get<Int>("eventId")?.let { eventId ->
            if(eventId != -1) {
                viewModelScope.launch {
                    repository.getEventById(eventId)?.also { event ->
                        currEvent = event
                        currentEventId = event.id
                        title = event.title
                        desc = event.desc
                        lat = event.lat
                        lng = event.lng
                        address = event.address.toString()
                        time = event.time
                    }
                }
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
                getDeviceLocation()

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
    fun getDeviceLocation() {
        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener {
                state = state.copy(
                    lastKnownLocation = it
                )
                lat = it.latitude
                lng = it.longitude
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