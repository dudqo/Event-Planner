package com.example.eventplanner.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.eventplanner.MapEvent
import com.example.eventplanner.domain.model.Event
import com.example.eventplanner.domain.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


class MapViewModel (): ViewModel() {

    var state by mutableStateOf(MapState())
    var longi by mutableStateOf("")
    var lati by mutableStateOf("")


    fun onEvent(event: MapEvent) {
        when(event) {
            is MapEvent.OnMapLongClick -> {
                lati = event.coord.latitude.toString()
                longi = event.coord.longitude.toString()
            }

            is MapEvent.onInfoWindowLongClick -> {

            }
        }
    }
}