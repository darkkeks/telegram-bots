package ru.darkkeks.telegram.hseremind

import org.springframework.stereotype.Component
import ru.darkkeks.telegram.core.createLogger
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Component
class NotifyService(
        val userRepository: UserRepository,
        val notificationSendService: NotificationSendService,
        val sourceFetchService: SourceFetchService,
        val itemFilterService: ItemFilterService
) {

    private val logger = createLogger<NotifyService>()

    private val alreadyNotified: MutableSet<NotificationRecord> = mutableSetOf()

    data class NotificationRecord(
            val user: Long,
            val chat: ChatSpec,
            val rule: RuleSpec,
            val lectureId: Int
    )

    fun update() = try {
        logger.info("Starting notify iteration")
        userRepository.findAll().forEach { user ->
            user.spec.chats.forEach { chat ->
                chat.rules.forEach { rule ->
                    val items = sourceFetchService.getSourceInfo(rule.source)
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
