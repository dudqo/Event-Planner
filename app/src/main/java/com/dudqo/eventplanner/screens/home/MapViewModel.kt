package com.dudqo.eventplanner.screens.home

import android.annotation.SuppressLint
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dudqo.eventplanner.MapEvent
import com.dudqo.eventplanner.domain.repository.EventRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MapViewModel @Inject constructor(
    private val repository: EventRepository,
): ViewModel() {

    var state by mutableStateOf(MapState())
    var lng by mutableStateOf(0.00)
    var lat by mutableStateOf(0.00)
    var longPressed by mutableStateOf(false)
    lateinit var fusedLocationClient: FusedLocationProviderClient

    init {
        viewModelScope.launch {
            repository.getEvents().collectLatest { events ->
                state = state.copy(
                    events = events
                )
            }
        }
    }

    fun onEvent(event: MapEvent) {
        when(event) {
            is MapEvent.OnMapLongClick -> {
                lat = event.coord.latitude
                lng = event.coord.longitude
                longPressed = true
            }

            is MapEvent.onInfoWindowLongClick -> {

            }

             is MapEvent.OnMarkerInfoClick -> {

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

}