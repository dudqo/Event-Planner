package com.example.eventplanner.screens

import com.example.eventplanner.domain.model.Event
import com.google.maps.android.compose.MapProperties

data class MapState(
    val properties: MapProperties = MapProperties(
        isMyLocationEnabled = false
    ),
    val eventLocation: List<Event> = emptyList()
)
