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

        val request = ruzApi.schedule("group", groupId,
                start = today.format(dateFormatter),
                finish = today.format(dateFormatter))

        val schedule = try {
            request.execute()
        } catch (e: Exception) {
            logger.error("Failed to get schedule", e)
            return
        }

        if (schedule.isSuccessful) {
            val items = schedule.body()

            if (items == null) {
                logger.warn("Schedule is null")
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

        for (item in lastSchedule) {
            val lectureDate = LocalDate.parse(item.date, dateFormatter)
            val lectureTime = LocalTime.parse(item.beginLesson, timeFormatter)

            val lectureStart = LocalDateTime.of(lectureDate, lectureTime)

            // Lecture starts 10 minutes (or less) from now
            if (currentTime.isBefore(lectureStart) &&
                    Duration.between(currentTime, lectureStart) <= Duration.ofMinutes(10)) {

                if (shouldNotify(item)) {
                    if (item.lessonOid !in alreadyNotified) {
                        alreadyNotified.add(item.lessonOid)

                        send(item, Duration.between(currentTime, lectureStart))
                    }
                }
            }
        }
    }

    private fun shouldNotify(item: ScheduleItem): Boolean {
        return item.dayOfWeek != 3 || "Основания алгебры и геометрии" in item.discipline
    }

    private fun send(item: ScheduleItem, startsIn: Duration) {
        val text = formatMessage(item, startsIn)

        logger.info("Sending notification:\n{}", text)

        telegramApi
                .sendMessage(chatId, text, parseMode = ParseMode.HTML, disableWebPagePreview = true)
                .executeChecked()
    }

    private fun formatMessage(item: ScheduleItem, startsIn: Duration): String {
        var additionalText = ""

        if (item.lecturer != null) {
            additionalText += "Лектор: ${item.lecturer}\n"
        }

        additionalText += formatUrl(item.url1, item.url1_description, "Ссылка")
        additionalText += formatUrl(item.url2, item.url2_description, "Ссылка #2")

        val minutes = startsIn.toMinutes() + 1

        return """
            <b>${item.discipline}</b>
            ${item.kindOfWork}
            Дата: ${item.date}
            Время: <b>${item.beginLesson} &#8212; ${item.endLesson}</b>
            Место: ${item.auditorium}
            
        """.trimIndent() + additionalText + """
            
            Начало через <b>$minutes мин.</b>
        """.trimIndent()
    }

    private fun formatUrl(url: String?, description: String?, default: String): String {
        url ?: return ""

        val name = description ?: default
        return if (name.endsWith("\n")) {
            "$name$url\n"
        } else {
            "$name: $url\n"
        }
    }
}

fun main(args: Array<String>) {
    val logger = createLogger<HseRemindApp>()
    val context = runApplication<HseRemindApp>(*args)

    val scheduler: ScheduledExecutorService = context.getBean()
    val service: NotifyService = context.getBean()

    scheduler.scheduleAtFixedRate(service::checkSchedule, 0, 60, TimeUnit.MINUTES)
    scheduler.scheduleAtFixedRate(service::notifyTelegram, 10, 60, TimeUnit.SECONDS)

    Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
        logger.error("Uncaught exception on thread {}", thread, throwable)
    }
}
