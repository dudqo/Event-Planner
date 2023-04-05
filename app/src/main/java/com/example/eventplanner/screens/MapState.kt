package com.example.eventplanner.screens

import android.location.Location
import com.example.eventplanner.domain.model.Event
import com.google.maps.android.compose.MapProperties

data class MapState(
    var properties: MapProperties = MapProperties(
        isMyLocationEnabled = false
    ),
    val eventLocation: List<Event> = emptyList(),
    var lastKnownLocation: Location? = null
)
