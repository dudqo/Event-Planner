package com.dudqo.eventplanner.screens.events

import com.dudqo.eventplanner.domain.model.Event

sealed class EventsEvent {

    data class OnTitleChange(val newTitle: String): EventsEvent()
    data class OnAddressChange(val newAddress: String): EventsEvent()
    data class OnDescChange(val newDesc: String): EventsEvent()
    data class OnUseCurrLocationChange(val newUseCurrLocation: Boolean): EventsEvent()
    data class OnDeleteEventClick(val event: Event): EventsEvent()

    object OnCreateEventClick: EventsEvent()
}
