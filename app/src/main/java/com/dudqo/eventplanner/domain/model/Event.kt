package com.dudqo.eventplanner.domain.model

data class Event(
    val id: Int?,
    val title: String,
    val lat: Double,
    val lng: Double,
    val address: String?,
    val desc: String,
    val time: String,
    val isPrivate: Boolean = true
)
