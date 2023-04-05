package com.example.eventplanner.screens

import android.annotation.SuppressLint
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.eventplanner.MapEvent
import com.example.eventplanner.domain.model.Event
import com.example.eventplanner.domain.repository.EventRepository
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


class MapViewModel (): ViewModel() {

    var state by mutableStateOf(MapState())
    var longi by mutableStateOf(0.00)
    var lati by mutableStateOf(0.00)
    var longPressed by mutableStateOf(false)


    fun onEvent(event: MapEvent) {
        when(event) {
            is MapEvent.OnMapLongClick -> {
                lati = event.coord.latitude
                longi = event.coord.longitude
                longPressed = true
            }

            is MapEvent.onInfoWindowLongClick -> {

            }
        }
    }

    @SuppressLint("MissingPermission")
    fun getDeviceLocation(
        fusedLocationProviderClient: FusedLocationProviderClient
    ) {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            val locationResult = fusedLocationProviderClient.lastLocation
            locationResult.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    state.lastKnownLocation = task.result
                }
            }
        } catch (e: SecurityException) {
            // Show error or something
        }
    }
}