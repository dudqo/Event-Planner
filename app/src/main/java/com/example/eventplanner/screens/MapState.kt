package com.example.eventplanner.screens

import com.google.maps.android.compose.MapProperties

data class MapState(
    val properties: MapProperties = MapProperties(
        isMyLocationEnabled = false
    )
)
