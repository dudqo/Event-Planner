package com.example.eventplanner.screens.events

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eventplanner.MapEvent
import com.example.eventplanner.domain.model.Event
import com.example.eventplanner.domain.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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
    var state by mutableStateOf(EventState())

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
                        Event(title, lat, lng, address, desc, time, isPrivate)
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
}