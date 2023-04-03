package com.example.eventplanner

import com.example.eventplanner.domain.model.Event
import com.google.android.gms.maps.model.LatLng

sealed class MapEvent {
    data class OnMapLongClick(val coord: LatLng): MapEvent()
    data class onInfoWindowLongClick(val locat: Event): MapEvent()
}