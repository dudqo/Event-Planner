package com.example.eventplanner.data

import androidx.room.Database

@Database(
    entities = [EventEntity::class],
    version = 1
)

abstract class EventDatabase {

    abstract val dao: EventDao
}