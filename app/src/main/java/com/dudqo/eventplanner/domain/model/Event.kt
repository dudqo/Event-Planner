package com.dudqo.eventplanner.domain.model

import android.graphics.Bitmap
import android.net.Uri

data class Event(
    val id: Int?,
    val title: String,
    val lat: Double,
    val lng: Double,
    val address: String?,
    val desc: String,
    val time: String,
    val isPrivate: Boolean = true,
    val images: List<Uri>
)
