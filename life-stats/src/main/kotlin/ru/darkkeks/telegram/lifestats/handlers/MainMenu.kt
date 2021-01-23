package ru.darkkeks.telegram.lifestats.handlers

import org.springframework.stereotype.Component
import ru.darkkeks.telegram.core.api.Telegram
import ru.darkkeks.telegram.lifestats.HandlerFactory
import ru.darkkeks.telegram.lifestats.handlerList

@Component
class MainMenu(val telegram: Telegram) : HandlerFactory {
    override fun handlers() = handlerList {
        command("help") {
            telegram.sendMessage(it.userData.chatId, "Тут /help")
        }

        command("start") {
            telegram.sendMessage(it.userData.chatId, "Тут /start")
        }

        fallback {
            telegram.sendMessage(it.userData.chatId, "Тут дефолт")
        }
    }
}
