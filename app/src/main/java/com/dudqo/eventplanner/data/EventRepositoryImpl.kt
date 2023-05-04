package com.dudqo.eventplanner.data

import com.dudqo.eventplanner.domain.model.Event
import com.dudqo.eventplanner.domain.repository.EventRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class EventRepositoryImpl(
    private val dao: EventDao
):EventRepository {
    override suspend fun insertEvent(event: Event) {
        dao.insertEvent(event.toEventEntity())
    }

    override suspend fun deleteEvent(event: Event) {
        dao.deleteEvent(event.toEventEntity())
    }

    override suspend fun getEvents(): Flow<List<Event>> {
        return dao.getEvents().map { events ->
            events.map {it.toEvent()}
        }
    }

    override suspend fun getEventById(id: Int): Event? {
        return dao.getEventById(id)
    }

}
