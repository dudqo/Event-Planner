package com.dudqo.eventplanner.screens.events

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.location.Geocoder
import android.net.Uri
import androidx.compose.runtime.*
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dudqo.eventplanner.AutocompleteResult
import com.dudqo.eventplanner.domain.model.Event
import com.dudqo.eventplanner.domain.repository.EventRepository
import com.dudqo.eventplanner.screens.sign_in.UserData
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@OptIn(ExperimentalPermissionsApi::class)
@HiltViewModel
class EventsViewModel @Inject constructor(
    private val repository: EventRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val userData = Firebase.auth.currentUser?.run {
        UserData(
            userId = uid,
            userEmail = email.toString(),
            userName = displayName,
            profilePictureUrl = photoUrl?.toString()
        )
    }
    var title by mutableStateOf("")
    var lat by mutableStateOf(0.00)
    var lng by mutableStateOf(0.00)
    var address by mutableStateOf("")
    var desc by mutableStateOf("")
    var time by mutableStateOf("")
    var timeInMillis by mutableStateOf(0L)
    var isPrivate by mutableStateOf(false)
    var useCurrLocation by mutableStateOf(false)
    var deleted by mutableStateOf(false)
    var currentEventId: Int? = null
    var state by mutableStateOf(EventState())
    var selectedImages by mutableStateOf<List<String>>(emptyList())
    var tempImages by mutableStateOf<List<String>>(emptyList())
    var uris by mutableStateOf<List<Uri>>(emptyList())
    private var job: Job? = null
    lateinit var currEvent: Event
    lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var geoCoder: Geocoder


    val locationAutofill = mutableStateListOf<AutocompleteResult>()
    lateinit var placesClient: PlacesClient
    var currMapLatLong by mutableStateOf(LatLng(0.0, 0.0))


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
                        selectedImages = event.images
                        uris = event.images.map { Uri.parse(it) }
                        tempImages = event.images
                        timeInMillis = event.timeInMillis
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
                if (useCurrLocation) {
                    address = ""
                    getDeviceLocation()
                    getAddress(
                        LatLng(
                            state.lastKnownLocation!!.latitude,
                            state.lastKnownLocation!!.longitude
                        )
                    )
                }

            }
            is EventsEvent.OnCreateEventClick -> {
                viewModelScope.launch {
                    if (useCurrLocation) {
                        repository.insertEvent(
                            Event(
                                currentEventId,
                                title,
                                state.lastKnownLocation!!.latitude,
                                state.lastKnownLocation!!.longitude,
                                address,
                                desc,
                                time,
                                isPrivate,
                                selectedImages,
                                timeInMillis
                            )
                        )
                    } else {
                        repository.insertEvent(
                            Event(
                                currentEventId,
                                title,
                                lat,
                                lng,
                                address,
                                desc,
                                time,
                                isPrivate,
                                selectedImages,
                                timeInMillis
                            )
                        )
                    }
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

    fun getAddress(latLng: LatLng) {
        viewModelScope.launch {
            val geocodeAddress = geoCoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            address = geocodeAddress?.get(0)?.getAddressLine(0).toString()
        }
    }

    fun deleteImages(uriList: List<String>) {
        for (uri in uriList) {
            deleted = false
            Uri.parse(uri).path?.let {deleted = File(it).delete() }
        }
    }
}