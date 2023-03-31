package com.example.eventplanner.domain.model

data class Event(
    val title: String,
    val lati: String,
    val longi : String,
    val address: String,
    val desc: String,
    val time: String,
    val isPublic: Boolean = true
)
