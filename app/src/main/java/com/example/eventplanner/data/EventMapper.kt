package com.example.eventplanner.data

import com.example.eventplanner.domain.model.Event

fun EventEntity.toEvent(): Event {
    return Event(
        title = title,
        lat = lat,
        lng = lng,
        address = address,
        desc = desc,
        time = time,
        isPrivate = isPrivate,
    )
}

fun Event.toEventEntity(): EventEntity {
    return EventEntity(
        title = title,
        lat = lat,
        lng = lng,
        address = address,
        desc = desc,
        time = time,
        isPrivate = isPrivate,
    )
}
