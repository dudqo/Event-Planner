package com.example.eventplanner.screens.events

import android.location.Location
import com.example.eventplanner.domain.model.Event

data class EventState(
    val events: List<Event> = emptyList(),
    val sorting: Boolean = false,
    var lastKnownLocation: Location? = null
)
