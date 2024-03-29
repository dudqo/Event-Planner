package com.dudqo.eventplanner.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity()
data class EventEntity(
    @PrimaryKey val id: Int? = null,
    val title: String,
    val lat: Double,
    val lng : Double,
    val address: String?,
    val desc: String,
    val time: String,
    val isPrivate: Boolean = true,
    @ColumnInfo(name = "images", defaultValue = "")
    val images: List<String>,
    @ColumnInfo(name = "timeInMillis", defaultValue = "0")
    val timeInMillis: Long
)
