package com.example.eventplanner.data

import androidx.room.*

import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: EventEntity)

    @Delete
    suspend fun deleteEvent(event: EventEntity)

    @Query("SELECT * FROM evententity")
    fun getEvents(): Flow<List<EventEntity>>



}
