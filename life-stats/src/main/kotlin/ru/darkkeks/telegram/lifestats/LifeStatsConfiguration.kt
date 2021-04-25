package ru.darkkeks.telegram.lifestats

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import ru.darkkeks.telegram.core.TelegramBotsConfiguration
import ru.darkkeks.telegram.core.api.PollingTelegramBot
import ru.darkkeks.telegram.core.api.Telegram
import ru.darkkeks.telegram.core.handle_wip.ButtonState
import ru.darkkeks.telegram.core.serialize.Registry
import ru.darkkeks.telegram.core.serialize.popInt
import ru.darkkeks.telegram.lifestats.handlers.EventClassButton
import java.util.concurrent.ScheduledExecutorService

@Configuration
@ComponentScan
@Import(TelegramBotsConfiguration::class)
class LifeStatsConfiguration {

    @Bean
    fun updateHandler(
        telegram: Telegram,
        lifeStatsUserDataProvider: LifeStatsUserDataProvider,
        buttonConverter: ButtonConverter,
        handlerFactories: List<HandlerFactory>,
    ): RoutingMessageHandler {
        return RoutingMessageHandler(
            telegram,
            lifeStatsUserDataProvider,
            buttonConverter,
            handlerFactories.flatMap { it.handlers() },
        )
    }

    @Bean
    fun telegramBot(
        telegram: Telegram,
        executorService: ScheduledExecutorService,
        messageHandler: RoutingMessageHandler,
    ): PollingTelegramBot {
        return PollingTelegramBot(telegram, executorService, messageHandler)
    }

    @Bean
    fun buttonStateRegistry() = Registry<ButtonState>().apply {
        register(0x01, EventClassButton::class) { EventClassButton(it.popInt()) }
    }

}
