package com.dudqo.eventplanner.data

import android.net.Uri
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class Converters {

    @TypeConverter
    fun fromUriList(value: List<Uri>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toUriList(value: String): List<Uri> {
        return try {
            Gson().fromJson(
                value,
                object : TypeToken<List<Uri>>() {}.type)
        } catch (e: Exception) {
            emptyList()
        }
    }
}