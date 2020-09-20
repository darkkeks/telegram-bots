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
import ru.darkkeks.telegram.core.*
import ru.darkkeks.telegram.core.api.ParseMode
import ru.darkkeks.telegram.core.api.TelegramApi
import ru.darkkeks.telegram.core.api.TelegramClientException
import ru.darkkeks.telegram.core.api.executeChecked
import java.io.File
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

object RuzUtils {
    val moscowZoneId: ZoneId = ZoneId.of("Europe/Moscow")
    val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
    val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")
}

@Component
class SourceFetchService(val ruzApi: RuzApi) {
    private val logger = createLogger<SourceFetchService>()

    private val sources: MutableMap<Source, Int> = mutableMapOf()
    private val sourceToId: MutableMap<Source, Int> = mutableMapOf()

    private val sourceResults: MutableMap<Source, List<ScheduleItem>> = mutableMapOf()

    fun addSource(source: Source) {
        logger.info("Adding source {}", source)
        synchronized(sources) {
            val count = sources[source] ?: 0
            sources[source] = count + 1
        }
    }

    fun removeSource(source: Source) {
        logger.info("Removing source {}", source)
        synchronized(sources) {
            val count = sources[source] ?: 0
            if (count > 1) {
                sources[source] = count - 1
            } else {
                sources.remove(source)
                sourceResults.remove(source)
            }
        }
    }

    fun update() {
        sources.keys.forEach { source ->
            var id = sourceToId[source]
            if (id == null) {
                id = fetchSourceId(source)
                if (id != null) {
                    sourceToId[source] = id
                }
            }

            if (id != null) {
                val result = fetchSourceInfo(source, id)
                if (result != null) {
                    sourceResults[source] = result
                }
            }
        }
    }

    private fun fetchSourceInfo(source: Source, id: Int): List<ScheduleItem>? {
        val (type, _) = sourceToTypeAndTerm(source) ?: return null

        val today = ZonedDateTime.now(RuzUtils.moscowZoneId)
        val formattedDate = today.format(RuzUtils.dateFormatter)

        logger.info("Fetching schedule for source {} with id {} and date {}", source, id, formattedDate)

        val request = ruzApi.schedule(type, id,
                start = today.format(RuzUtils.dateFormatter),
                finish = today.format(RuzUtils.dateFormatter))

        val response = try {
            request.execute()
        } catch (e: Exception) {
            logger.warn("Failed to fetch source schedule", e)
            return null
        }

        return if (response.isSuccessful) {
            val result = response.body()
            logger.info("Successfully fetched {} schedule items for date {}", result?.size ?: 0, formattedDate)
            result
        } else {
            logger.error("Failed to get schedule: ", response.errorBody())
            null
        }
    }

    private fun fetchSourceId(source: Source): Int? {
        val (type, term) = sourceToTypeAndTerm(source) ?: return null

        val response = try {
            ruzApi.search(term, type).execute()
        } catch (e: Exception) {
            logger.warn("Failed to fetch source id", e)
            return null
        }

        return if (response.isSuccessful) {
            response.body()?.firstOrNull()?.id
        } else {
            logger.warn("Failed to fetch source id", response.errorBody())
            null
        }
    }

    private fun sourceToTypeAndTerm(source: Source): Pair<String, String>? {
        return when (source) {
            is StudentSource -> "student" to source.student
            is GroupSource -> "group" to source.group
            else -> return null
        }
    }

    fun getSourceInfo(source: Source): List<ScheduleItem> {
        return sourceResults[source] ?: listOf()
    }
}

@Component
class NotificationSendService(val telegramApi: TelegramApi) {

    private val logger = createLogger<NotificationSendService>()

    fun notify(chatId: Long, item: ScheduleItem, startsIn: Duration) {
        val text = formatMessage(item, startsIn)

        logger.info("Sending notification:\n{}", text)

        try {
            telegramApi
                    .sendMessage(chatId, text, parseMode = ParseMode.HTML, disableWebPagePreview = true)
                    .executeChecked()
        } catch (e: TelegramClientException) {
            logger.warn("Failed to send notification to chat {}", chatId, e)
        }
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

@Component
class UserRepository {

    private var users: MutableMap<Long, User> = mutableMapOf()

    fun getAll(): Collection<User> {
        return users.values
    }

    fun get(id: Long): User? {
        return users[id]
    }

    fun save(user: User) {
        users[user.id] = user
    }
}

@Component
class ItemFilterService {
    fun shouldNotify(item: ScheduleItem, filter: Filter): Boolean {
        return when (filter) {
            is LectureNameFilter -> {
                filter.lectureName.toRegex(RegexOption.IGNORE_CASE).matches(item.discipline)
            }
            is WeekDaysFilter -> {
                val now = LocalDateTime.now(RuzUtils.moscowZoneId)
                now.dayOfWeek.value in filter.weekDays
            }
            is LecturerNameFilter -> {
                item.lecturer ?: return false
                filter.lecturerName.toRegex(RegexOption.IGNORE_CASE).matches(item.lecturer)
            }
            is LectureTypeFilter -> {
                filter.lectureType.trim() == item.kindOfWork.trim()
            }
            is AllOfFilter -> {
                filter.allOf.all { shouldNotify(item, it) }
            }
            is AnyOfFilter -> {
                filter.anyOf.any { shouldNotify(item, it) }
            }
            is NoneOfFilter -> {
                filter.noneOf.none { shouldNotify(item, it) }
            }
            else -> false
        }
    }
}

@Component
class NotifyService(
        val userRepository: UserRepository,
        val notificationSendService: NotificationSendService,
        val sourceFetchService: SourceFetchService,
        val itemFilterService: ItemFilterService
) {

    private val logger = createLogger<NotifyService>()

    private val alreadyNotified: MutableSet<Int> = mutableSetOf()

    fun update() = try {
        logger.info("Starting notify iteration")
        userRepository.getAll().forEach { user ->
            user.spec.chats.forEach { chat ->
                chat.rules.forEach { rule ->
                    val source = rule.source
                    val items = sourceFetchService.getSourceInfo(source)
                    items.forEach { item ->
                        processItem(user, chat, rule, item)
                    }
                }
            }
        }
        logger.info("Done")
    } catch (e: Exception) {
        logger.warn("Exception during notify iteration", e)
    }

    fun processItem(
            user: User,
            chat: ChatSpec,
            rule: RuleSpec,
            item: ScheduleItem
    ) {
        if (rule.filter == null || itemFilterService.shouldNotify(item, rule.filter)) {
            val currentTime = LocalDateTime.now(RuzUtils.moscowZoneId)

            val lectureDate = LocalDate.parse(item.date, RuzUtils.dateFormatter)
            val lectureTime = LocalTime.parse(item.beginLesson, RuzUtils.timeFormatter)

            val lectureStart = LocalDateTime.of(lectureDate, lectureTime)

            // Lecture starts 10 minutes (or less) from now
            if (currentTime.isBefore(lectureStart) &&
                    Duration.between(currentTime, lectureStart) <= Duration.ofMinutes(10)) {

                if (item.lessonOid !in alreadyNotified) {
                    alreadyNotified.add(item.lessonOid)

                    val chatId = targetToChatId(user, chat.target)
                    if (chatId != null) {
                        notificationSendService.notify(chatId, item, Duration.between(currentTime, lectureStart))
                    }
                }
            }
        }
    }

    fun targetToChatId(user: User, target: Target): Long? {
        return when (target) {
            is GroupTarget -> target.group
            is ChannelTarget -> target.channel
            is MeTarget -> user.id
            else -> null
        }
    }
}

fun main(args: Array<String>) {
    val logger = createLogger<HseRemindApp>()

    Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
        logger.error("Uncaught exception on thread {}", thread, throwable)
    }

    val context = runApplication<HseRemindApp>(*args)

    val scheduler: ScheduledExecutorService = context.getBean()

    val notifyService: NotifyService = context.getBean()
    val sourceFetchService: SourceFetchService = context.getBean()

    scheduler.scheduleAtFixedRate(sourceFetchService::update, 0, 60, TimeUnit.MINUTES)
    scheduler.scheduleAtFixedRate(notifyService::update, 10, 60, TimeUnit.SECONDS)
}
