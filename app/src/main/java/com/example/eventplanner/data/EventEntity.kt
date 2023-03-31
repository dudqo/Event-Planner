package com.example.eventplanner.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity()
data class EventEntity(
    @PrimaryKey val id: Int? = null,
    val title: String,
    val lati: String,
    val longi : String,
    val address: String,
    val desc: String,
    val time: String,
    val isPublic: Boolean = true
)
