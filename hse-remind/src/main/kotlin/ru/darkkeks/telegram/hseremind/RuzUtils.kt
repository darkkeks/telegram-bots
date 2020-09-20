package ru.darkkeks.telegram.hseremind

import java.time.ZoneId
import java.time.format.DateTimeFormatter

object RuzUtils {
    val moscowZoneId: ZoneId = ZoneId.of("Europe/Moscow")
    val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
    val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")
}
