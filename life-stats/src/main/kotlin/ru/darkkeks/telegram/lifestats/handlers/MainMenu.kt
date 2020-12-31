package ru.darkkeks.telegram.lifestats.handlers

import org.springframework.stereotype.Component
import ru.darkkeks.telegram.core.api.InlineKeyboardButton
import ru.darkkeks.telegram.core.api.InlineKeyboardMarkup
import ru.darkkeks.telegram.core.api.KeyboardButton
import ru.darkkeks.telegram.core.api.ReplyKeyboardMarkup
import ru.darkkeks.telegram.core.api.Telegram
import ru.darkkeks.telegram.core.api.executeChecked
import ru.darkkeks.telegram.core.api.executeTelegram
import ru.darkkeks.telegram.core.buildKeyboard
import ru.darkkeks.telegram.core.toJsonPretty
import ru.darkkeks.telegram.lifestats.Handler
import ru.darkkeks.telegram.lifestats.HandlerFactory
import ru.darkkeks.telegram.lifestats.MessageContext
import ru.darkkeks.telegram.lifestats.MessageHandler
import ru.darkkeks.telegram.lifestats.handlerList

@Component
class MainMenu(
    val telegram: Telegram
) : HandlerFactory {
    override fun handlers() = handlerList {
        command("start") { context ->
            val keyboard = InlineKeyboardMarkup(buildList {
                add(buildList {
                    add(InlineKeyboardButton("😏 Кнопка", callbackData = "a button"))
                })
            })
            telegram.sendMessage(context.userData.chatId, "Hi!", replyMarkup = keyboard)
        }
    }
}
