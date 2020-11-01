package ru.darkkeks.telegram.hseremind.ruz

import org.springframework.stereotype.Component
import ru.darkkeks.telegram.core.api.ParseMode
import ru.darkkeks.telegram.core.api.Telegram
import ru.darkkeks.telegram.core.api.TelegramClientException
import ru.darkkeks.telegram.core.api.executeChecked
import ru.darkkeks.telegram.core.createLogger
import java.time.Duration

@Component
class RuzNotificationSendService(val telegram: Telegram) {

    private val logger = createLogger<RuzNotificationSendService>()

    fun notify(chatId: Long, item: ScheduleItem, startsIn: Duration) {
        val text = formatMessage(item, startsIn)

        logger.info("Sending notification:\n{}", text)

        try {
            telegram
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
            ${item.kindOfWork ?: ""}
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
