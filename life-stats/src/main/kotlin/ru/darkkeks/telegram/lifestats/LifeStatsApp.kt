package ru.darkkeks.telegram.lifestats

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import ru.darkkeks.telegram.core.api.PollingTelegramBot

@SpringBootApplication
class LifeStatsApp

fun main(args: Array<String>) {
    val context = runApplication<LifeStatsApp>(*args)
    val bot = context.getBean(PollingTelegramBot::class.java)
    bot.startLongPolling()
}
