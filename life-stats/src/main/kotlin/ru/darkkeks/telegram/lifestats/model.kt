package ru.darkkeks.telegram.lifestats

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

enum class EventType {
    POINT,  // point in time
    DURATION,  // amount of time
    SEGMENT,  // begin and end
    COUNT,  // just a number
}

@Table("event_classes")
class EventClass (
    @Id
    val ecid: Int,
    val textId: String,
    val name: String,
    val description: String,
    val type: EventType,
)

@Table("events")
class Event(
    @Id
    val eid: Int,
    val ecid: Int,
    val begin: Instant,
    val end: Instant,
    val count: Long,
    val data: String,
)
