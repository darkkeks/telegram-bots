package ru.darkkeks.telegram.hseremind.ruz

import org.springframework.stereotype.Component
import ru.darkkeks.telegram.core.createLogger
import ru.darkkeks.telegram.hseremind.*
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Component
class RuzNotifyService(
        val userConfigService: UserConfigService,
        val userRepository: UserRepository,
        val ruzNotificationSendService: RuzNotificationSendService,
        val ruzSourceFetchService: RuzSourceFetchService,
        val itemFilterService: ItemFilterService
) {

    private val logger = createLogger<RuzNotifyService>()

    private val alreadyNotified: MutableSet<NotificationRecord> = mutableSetOf()

    data class NotificationRecord(
            val user: Long,
            val chat: ChatSpec,
            val rule: RuzRuleSpec,
            val lectureId: Int
    )

    fun update() = try {
        logger.info("Starting notify iteration")
        userRepository.findAll().forEach { user ->
            val spec = safeParseSpec(user.spec)
            spec?.chats?.forEach { chat ->
                (chat.lectureRules ?: chat.rules)?.forEach { rule ->
                    val items = ruzSourceFetchService.getSourceInfo(rule.source)
                    items?.forEach { item ->
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
            rule: RuzRuleSpec,
            item: ScheduleItem
    ) {
        val currentTime = LocalDateTime.now(RuzUtils.moscowZoneId)

        val lectureDate = LocalDate.parse(item.date, RuzUtils.dateFormatter)
        val lectureTime = LocalTime.parse(item.beginLesson, RuzUtils.timeFormatter)

        if (rule.filter == null ||
                itemFilterService.shouldNotify(item, rule.filter, LocalDateTime.of(lectureDate, lectureTime))) {

            val lectureStart = LocalDateTime.of(lectureDate, lectureTime)

            // Lecture starts 10 minutes (or less) from now
            if (currentTime.isBefore(lectureStart) &&
                    Duration.between(currentTime, lectureStart) <= Duration.ofMinutes(10)) {

                val record = NotificationRecord(user.id, chat, rule, item.lessonOid)
                if (record !in alreadyNotified) {
                    alreadyNotified.add(record)

                    val chatId = targetToChatId(user, chat.target)
                    if (chatId != null) {
                        ruzNotificationSendService.notify(chatId, item, Duration.between(currentTime, lectureStart))
                    }
                }
            }
        }
    }
}
