package ru.darkkeks.telegram.trackyoursheet

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import ru.darkkeks.telegram.core.TelegramBotsConfiguration
import ru.darkkeks.telegram.core.api.PollingTelegramBot
import ru.darkkeks.telegram.core.api.TelegramApi
import ru.darkkeks.telegram.core.api.UpdateHandler
import ru.darkkeks.telegram.core.handle.CommonUpdateHandler
import ru.darkkeks.telegram.core.objectMapper
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService

@Configuration
@Import(TelegramBotsConfiguration::class)
class TrackYourSheetConfiguration {

    @Bean
    fun scheduledExecutorService(): ScheduledExecutorService = Executors.newScheduledThreadPool(4)

    @Bean
    fun telegramApi(@Value("\${telegram.base_url}") baseUrl: String,
                    @Value("\${telegram.token}") telegramToken: String): TelegramApi {
        return Retrofit.Builder()
                .baseUrl("$baseUrl$telegramToken/")
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .build()
                .create(TelegramApi::class.java)
    }

    @Bean
    fun telegramBot(
            telegramApi: TelegramApi,
            executorService: ScheduledExecutorService,
            commonUpdateHandler: CommonUpdateHandler
    ): PollingTelegramBot {
        return PollingTelegramBot(telegramApi, executorService, commonUpdateHandler)
    }
}
