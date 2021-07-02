package ru.darkkeks.telegram.lifestats

import org.springframework.data.annotation.Id
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant
import javax.annotation.processing.Generated

enum class EventType {
    POINT,  // point in time
//    DURATION,  // amount of time // TODO обдумать
    SEGMENT,  // begin and end
    COUNT,  // just a number
    ;

    companion object {
        fun ofString(value: String): EventType? {
            return values().firstOrNull { it.name == value.uppercase() }
        }
    }
}

@Table("users")
data class User(
    @Id
    val uid: Long,
    val state: String,
    val stateData: String,
)

@Table("event_classes")
data class EventClass(
    @Id
    val ecid: Int = 0,
    val uid: Long,
    val textId: String,
    val name: String,
    val description: String?,
    val type: EventType,
)

/**
 * Описание одного события. Содержит в себе тип события [ecid].
 * - Для событий [EventType.POINT] хранит [begin] = [end] = точке во времени.
 * - Для событий [EventType.DURATION] хранит такие [begin] и [end], что [end] - [begin] равно нужному временному отрезку.
 * - Для событий [EventType.SEGMENT] хранит отрезок времени от [begin] до [end].
 * - Для событий [EventType.COUNT] хранит только [count].
 */
@Table("events")
data class Event(
    @Id
    val eid: Int = 0,
    val ecid: Int,
    val begin: Instant?,
    val end: Instant?,
    val count: Long?,
    val comment: String?,
)
