package ru.darkkeks.telegram.hseremind

import org.springframework.beans.factory.getBean
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import ru.darkkeks.telegram.core.api.PollingTelegramBot
import ru.darkkeks.telegram.core.createLogger
import ru.darkkeks.telegram.hseremind.ruz.RuzNotifyService
import ru.darkkeks.telegram.hseremind.youtube.YoutubeNotifyService
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

    val userConfigService: UserConfigService = context.getBean()
    userConfigService.load()

    val sourceFetchers = context.getBeansOfType(SourceFetchService::class.java)
    sourceFetchers.values.forEach {
        scheduler.scheduleAtFixedRate(it::update, 0, 60, TimeUnit.MINUTES)
    }

    val ruzNotifyService: RuzNotifyService = context.getBean()
    scheduler.scheduleAtFixedRate(ruzNotifyService::update, 10, 60, TimeUnit.SECONDS)

    val youtubeNotifyService: YoutubeNotifyService = context.getBean()
    scheduler.scheduleAtFixedRate(youtubeNotifyService::update, 10, 60, TimeUnit.SECONDS)

    val pollingTelegramBot: PollingTelegramBot = context.getBean()
    pollingTelegramBot.startLongPolling()
}
