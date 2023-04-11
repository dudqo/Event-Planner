package com.example.eventplanner.screens.events

import com.example.eventplanner.domain.model.Event

data class EventState(
    val events: List<Event> = emptyList(),
    val sorting: Boolean = false
)
