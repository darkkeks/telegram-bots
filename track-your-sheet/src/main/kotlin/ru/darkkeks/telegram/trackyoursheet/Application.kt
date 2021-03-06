package ru.darkkeks.telegram.trackyoursheet

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import ru.darkkeks.telegram.core.api.PollingTelegramBot


@SpringBootApplication
class TrackYourSheetApp

fun main(args: Array<String>) {
    val context = runApplication<TrackYourSheetApp>(*args)

    val bot = context.getBean(PollingTelegramBot::class.java)
    bot.startLongPolling()
}
