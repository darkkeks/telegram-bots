package ru.darkkeks.telegram.hseremind

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.beans.factory.annotation.Value
import org.springframework.beans.factory.getBean
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.PropertySource
import org.springframework.stereotype.Component
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import ru.darkkeks.telegram.core.TelegramBotsConfiguration
import ru.darkkeks.telegram.core.api.*
import ru.darkkeks.telegram.core.createLogger
import ru.darkkeks.telegram.core.objectMapper
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

@SpringBootApplication
class HseRemindApp

@Configuration
@Import(TelegramBotsConfiguration::class)
class HseRemindConfiguration {
    @Bean
    fun scheduledExecutorService(): ScheduledExecutorService = Executors.newScheduledThreadPool(4)

    @Bean
    fun telegramApi(@Value("\${telegram.base_url}") baseUrl: String,
                    @Value("\${telegram.token}") telegramToken: String): TelegramApi {
        return Retrofit.Builder()
                .baseUrl("$baseUrl$telegramToken/")
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .build()
                .create(TelegramApi::class.java)
    }

    @Bean
    fun ruzApi(@Value("\${ruz.base_url}") baseUrl: String): RuzApi {
        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(JacksonConverterFactory.create(ObjectMapper().registerModule(KotlinModule())))
                .build()
                .create(RuzApi::class.java)
    }
}

@Component
class NotifyService(
        val telegramApi: TelegramApi,
        val ruzApi: RuzApi,
        @Value("\${notify.group_id}") val groupId: Int,
        @Value("\${notify.chat_id}") val chatId: Long
) {

    private val logger = createLogger<NotifyService>()

    private final val moscowZoneId: ZoneId = ZoneId.of("Europe/Moscow")
    private val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
    private val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    private val alreadyNotified: MutableSet<Int> = mutableSetOf()
    private var lastSchedule: List<ScheduleItem> = listOf()

    fun checkSchedule() {
        val today = ZonedDateTime.now(moscowZoneId)
        val formattedDate = today.format(dateFormatter)

        logger.info("Getting schedule for date {}", formattedDate)

        val request = ruzApi.schedule("group", groupId, today.format(dateFormatter), today.format(dateFormatter))
        val schedule = try {
            request.execute()
        } catch (e: Exception) {
            logger.error("Failed to get schedule", e)
            return
        }

        if (schedule.isSuccessful) {
            val items = schedule.body()

            if (items == null) {
                logger.warn("Schedule is null ?!?")
                return
            }

            logger.info("Successfully fetched {} schedule items for date {}", items.size, formattedDate)

            lastSchedule = items
        } else {
            logger.error("Failed to get schedule: ", schedule.errorBody())
        }
    }

    fun notifyTelegram() {
        val currentTime = LocalDateTime.now(moscowZoneId)

        logger.info("Starting notify iteration. Schedule size is {}", lastSchedule.size)

        for (item in lastSchedule) {
            val lectureDate = LocalDate.parse(item.date, dateFormatter)
            val lectureTime = LocalTime.parse(item.beginLesson, timeFormatter)

            val lectureStart = LocalDateTime.of(lectureDate, lectureTime)

            // Lecture starts 10 minutes (or less) from now
            if (currentTime.isBefore(lectureStart) &&
                    Duration.between(currentTime, lectureStart) <= Duration.ofMinutes(10)) {

                if (item.lessonOid !in alreadyNotified) {
                    alreadyNotified.add(item.lessonOid)

                    send(item, Duration.between(currentTime, lectureStart))
                }
            }
        }
    }

    private fun send(item: ScheduleItem, startsIn: Duration) {
        var text = """
            <b>${item.discipline}</b>
            Дата: ${item.date}
            Время: <b>${item.beginLesson} &#8212; ${item.endLesson}</b>
            Место: ${item.auditorium}
        """.trimIndent()

        if (item.url1 != null) {
            text += "\nСсылка: ${item.url1}"
        }

        if (item.url2 != null) {
            text += "\nСсылка #2: ${item.url2}"
        }

        text += """
            
            
            Начало через <b>${startsIn.toMinutes()} мин.</b>!
        """.trimIndent()

        logger.info("Sending notification:\n{}", text)

        try {
            telegramApi.sendMessage(
                    chatId,
                    text,
                    parseMode = ParseMode.HTML,
                    disableWebPagePreview = true
            ).executeChecked()
        } catch (e: TelegramClientException) {
            logger.error("Failed to send notification", e)
        }
    }
}

fun main(args: Array<String>) {
    val logger = createLogger<HseRemindApp>()
    val context = runApplication<HseRemindApp>(*args)

    val scheduler: ScheduledExecutorService = context.getBean()
    val service: NotifyService = context.getBean()

    scheduler.scheduleAtFixedRate(service::checkSchedule, 0, 1, TimeUnit.HOURS)
    scheduler.scheduleAtFixedRate(service::notifyTelegram, 15, 60, TimeUnit.SECONDS)

    Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
        logger.error("Uncaught exception on thread {}", thread, throwable)
    }
}
