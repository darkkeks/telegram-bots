package ru.darkkeks.telegram.hseremind

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import ru.darkkeks.telegram.core.TelegramBotsConfiguration
import ru.darkkeks.telegram.core.api.PollingTelegramBot
import ru.darkkeks.telegram.core.api.Telegram
import ru.darkkeks.telegram.core.api.TelegramFiles
import ru.darkkeks.telegram.core.createLogger
import ru.darkkeks.telegram.core.objectMapper
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService


@Configuration
@Import(TelegramBotsConfiguration::class)
class HseRemindConfiguration {

    @Bean
    fun scheduledExecutorService(): ScheduledExecutorService = Executors.newScheduledThreadPool(4)

    @Bean
    fun okHttpClient(): OkHttpClient {
        val logger = createLogger<Retrofit>()
        val interceptor = HttpLoggingInterceptor { message -> logger.info(message) }

        interceptor.level = HttpLoggingInterceptor.Level.BASIC
        return OkHttpClient.Builder()
//                .addInterceptor(interceptor)
                .build()
    }

    @Bean
    fun telegram(okHttpClient: OkHttpClient,
                 @Value("\${telegram.base_url}") baseUrl: String,
                 @Value("\${telegram.token}") telegramToken: String): Telegram {
        return Retrofit.Builder()
                .baseUrl("$baseUrl$telegramToken/")
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .client(okHttpClient)
                .build()
                .create(Telegram::class.java)
    }

    @Bean
    fun telegramFiles(okHttpClient: OkHttpClient,
                      @Value("\${telegram.file_url}") baseUrl: String,
                      @Value("\${telegram.token}") telegramToken: String): TelegramFiles {
        return Retrofit.Builder()
                .baseUrl("$baseUrl$telegramToken/")
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .client(okHttpClient)
                .build()
                .create(TelegramFiles::class.java)
    }

    @Bean
    fun ruzApi(okHttpClient: OkHttpClient,
               @Value("\${ruz.base_url}") baseUrl: String): RuzApi {
        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(JacksonConverterFactory.create(ObjectMapper().registerModule(KotlinModule())))
                .client(okHttpClient)
                .build()
                .create(RuzApi::class.java)
    }

    @Bean
    fun telegramBot(
            telegram: Telegram,
            executorService: ScheduledExecutorService,
            hseRemindUpdateHandler: HseRemindUpdateHandler
    ): PollingTelegramBot {
        return PollingTelegramBot(telegram, executorService, hseRemindUpdateHandler)
    }
}
