package ru.darkkeks.telegram.core

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.PrettyPrinter
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.ObjectWriter
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import retrofit2.Call
import ru.darkkeks.telegram.core.api.TelegramClientException
import ru.darkkeks.telegram.core.api.TelegramResponse
import java.io.IOException
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*
import kotlin.reflect.KClass

fun <T : Any> createLogger(kclass: KClass<T>): Logger {
    return LoggerFactory.getLogger(kclass.java)
}

inline fun <reified T> createLogger(): Logger {
    return LoggerFactory.getLogger(T::class.java)
}


val formatter: DateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
        .withLocale(Locale("ru"))
        .withZone(TimeZone.getTimeZone("Europe/Moscow").toZoneId())

fun formatTime(time: Instant): String = formatter.format(time)


val objectMapper: ObjectMapper = ObjectMapper()
        .registerModule(KotlinModule())
        .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        .setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL)

val prettyWriter: ObjectWriter = objectMapper.writerWithDefaultPrettyPrinter()

inline fun <reified T> fromJson(data: String): T {
    return objectMapper.readValue(data, T::class.java)
}

fun <T> toJson(value: T): String {
    return objectMapper.writeValueAsString(value)
}

fun <T> toJsonPretty(value: T): String {
    return prettyWriter.writeValueAsString(value);
}
