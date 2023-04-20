package com.dudqo.eventplanner.screens.events

import android.location.Location
import com.dudqo.eventplanner.domain.model.Event

data class EventState(
    val events: List<Event> = emptyList(),
    val sorting: Boolean = false,
    var lastKnownLocation: Location? = null
)
