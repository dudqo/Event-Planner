package com.dudqo.eventplanner.data

import com.dudqo.eventplanner.domain.model.Event

fun EventEntity.toEvent(): Event {
    return Event(
        id = id,
        title = title,
        lat = lat,
        lng = lng,
        address = address,
        desc = desc,
        time = time,
        isPrivate = isPrivate,
        images = images,
        timeInMillis = timeInMillis
    )
}

fun Event.toEventEntity(): EventEntity {
    return EventEntity(
        id = id,
        title = title,
        lat = lat,
        lng = lng,
        address = address,
        desc = desc,
        time = time,
        isPrivate = isPrivate,
        images = images,
        timeInMillis = timeInMillis
    )
}
