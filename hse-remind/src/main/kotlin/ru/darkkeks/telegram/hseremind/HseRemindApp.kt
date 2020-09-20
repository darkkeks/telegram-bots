package ru.darkkeks.telegram.hseremind

import org.springframework.beans.factory.getBean
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import ru.darkkeks.telegram.core.*
import ru.darkkeks.telegram.core.api.PollingTelegramBot
import ru.darkkeks.telegram.core.api.Telegram
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

@SpringBootApplication
class HseRemindApp

fun main(args: Array<String>) {
    val logger = createLogger<HseRemindApp>()

    Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
        logger.error("Uncaught exception on thread {}", thread, throwable)
    }

    val context = runApplication<HseRemindApp>(*args)

    val scheduler: ScheduledExecutorService = context.getBean()

    val notifyService: NotifyService = context.getBean()
    val userConfigService: UserConfigService = context.getBean()
    val sourceFetchService: SourceFetchService = context.getBean()
    val pollingTelegramBot: PollingTelegramBot = context.getBean()

    userConfigService.load()

    scheduler.scheduleAtFixedRate(sourceFetchService::update, 0, 60, TimeUnit.MINUTES)
    scheduler.scheduleAtFixedRate(notifyService::update, 10, 60, TimeUnit.SECONDS)

    pollingTelegramBot.startLongPolling()
}
