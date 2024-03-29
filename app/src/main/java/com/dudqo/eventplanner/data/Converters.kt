package com.dudqo.eventplanner.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class Converters {

    @TypeConverter
    fun fromUriList(value: List<String>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toUriList(value: String): List<String> {
        return try {
            Gson().fromJson(
                value,
                object : TypeToken<List<String>>() {}.type)
        } catch (e: Exception) {
            emptyList()
        }
    }
}