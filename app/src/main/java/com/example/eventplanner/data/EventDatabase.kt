package com.example.eventplanner.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [EventEntity::class],
    version = 1
)

abstract class EventDatabase: RoomDatabase() {

    abstract val dao: EventDao
}
