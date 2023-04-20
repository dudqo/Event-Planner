package com.dudqo.eventplanner.screens.home

import android.location.Location
import com.dudqo.eventplanner.domain.model.Event
import com.google.maps.android.compose.MapProperties

data class MapState(
    var properties: MapProperties = MapProperties(
        isMyLocationEnabled = false
    ),
    val events: List<Event> = emptyList(),
    var lastKnownLocation: Location? = null
)
