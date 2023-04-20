package com.dudqo.eventplanner.domain.repository

import com.dudqo.eventplanner.domain.model.Event
import kotlinx.coroutines.flow.Flow

interface EventRepository {

    suspend fun insertEvent(event: Event)

    suspend fun deleteEvent(event: Event)

    suspend fun getEvents(): Flow<List<Event>>

    suspend fun getEventById(id: Int): Event?
}