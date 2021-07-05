package ru.darkkeks.telegram.lifestats.handlers

import org.springframework.stereotype.Component
import ru.darkkeks.telegram.core.api.Telegram
import ru.darkkeks.telegram.core.buildKeyboard
import ru.darkkeks.telegram.lifestats.ButtonConverter
import ru.darkkeks.telegram.lifestats.Context
import ru.darkkeks.telegram.lifestats.EventClassesButton
import ru.darkkeks.telegram.lifestats.ReportButton
import ru.darkkeks.telegram.lifestats.resetState
import ru.darkkeks.telegram.lifestats.service.UserService

@Component
class CommonMessages(
    private val telegram: Telegram,
    private val userService: UserService,
    private val buttonConverter: ButtonConverter,
) {
    fun enterMainMenuState(context: Context) {
        userService.saveUserData(context.user.resetState())
        sendMainMenuMessage(context)
    }

    fun sendMainMenuMessage(context: Context) {
        val keyboard = buildKeyboard {
            add(ReportButton())
            add(EventClassesButton())
        }
        telegram.sendMessage(
            context.user.chatId,
            """
                Здесь должен быть main текст.
            """.trimIndent(),
            replyMarkup = buttonConverter.serialize(keyboard),
        )
    }
}
