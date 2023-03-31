package com.example.eventplanner.data

import com.example.eventplanner.domain.model.Event

fun EventEntity.toEvent(): Event {
    return Event(
        title = title,
        lati = lati,
        longi = longi,
        address = address,
        desc = desc,
        time = time,
        isPublic = isPublic,
    )
}

fun Event.toEventEntity(): Event {
    return Event(
        title = title,
        lati = lati,
        longi = longi,
        address = address,
        desc = desc,
        time = time,
        isPublic = isPublic,
    )
}