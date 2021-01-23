package ru.darkkeks.telegram.lifestats

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import ru.darkkeks.telegram.core.TelegramBotsConfiguration
import ru.darkkeks.telegram.core.api.PollingTelegramBot
import ru.darkkeks.telegram.core.api.Telegram
import ru.darkkeks.telegram.core.createLogger
import ru.darkkeks.telegram.core.handle_wip.MessageState
import ru.darkkeks.telegram.core.serialize.Registry
import java.util.concurrent.ScheduledExecutorService

@Configuration
@ComponentScan
@Import(TelegramBotsConfiguration::class)
class LifeStatsConfiguration {

    private val logger = createLogger<LifeStatsConfiguration>()

    @Bean
    fun updateHandler(
        lifeStatsUserDataProvider: LifeStatsUserDataProvider,
        handlerFactories: List<HandlerFactory>
    ): RoutingMessageHandler {
        return RoutingMessageHandler(
            lifeStatsUserDataProvider,
            handlerFactories.flatMap { it.handlers() }
        )
    }

    @Bean
    fun telegramBot(
        telegram: Telegram,
        executorService: ScheduledExecutorService,
        messageHandler: RoutingMessageHandler
    ): PollingTelegramBot {
        return PollingTelegramBot(telegram, executorService, messageHandler)
    }

    @Bean
    fun messageStateRegistry() = Registry<MessageState>().apply {

    }

}
