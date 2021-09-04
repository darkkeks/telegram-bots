package ru.darkkeks.telegram.lifestats

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import ru.darkkeks.telegram.core.ButtonConverter
import ru.darkkeks.telegram.core.HandlerFactory
import ru.darkkeks.telegram.core.RoutingUpdateHandler
import ru.darkkeks.telegram.core.TelegramBotsConfiguration
import ru.darkkeks.telegram.core.UserDataProvider
import ru.darkkeks.telegram.core.api.PollingTelegramBot
import ru.darkkeks.telegram.core.api.Telegram
import java.util.concurrent.ScheduledExecutorService

@Configuration
@ComponentScan
@Import(
    TelegramBotsConfiguration::class,
    ButtonConfiguration::class,
)
class LifeStatsConfiguration {

    @Bean
    fun updateHandler(
        telegram: Telegram,
        userDataProvider: UserDataProvider,
        buttonConverter: ButtonConverter,
        handlerFactories: List<HandlerFactory>,
    ): RoutingUpdateHandler {
        return RoutingUpdateHandler(
            telegram,
            userDataProvider,
            buttonConverter,
            handlerFactories.flatMap { it.handlers() },
        )
    }

    @Bean
    fun telegramBot(
        telegram: Telegram,
        executorService: ScheduledExecutorService,
        messageHandler: RoutingUpdateHandler,
    ): PollingTelegramBot {
        return PollingTelegramBot(telegram, executorService, messageHandler)
    }

}
