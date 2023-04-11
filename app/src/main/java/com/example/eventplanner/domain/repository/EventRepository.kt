package com.example.eventplanner.domain.repository

import com.example.eventplanner.domain.model.Event
import kotlinx.coroutines.flow.Flow

interface EventRepository {

    suspend fun insertEvent(event: Event)

    suspend fun deleteEvent(event: Event)

    suspend fun getEvents(): Flow<List<Event>>
}