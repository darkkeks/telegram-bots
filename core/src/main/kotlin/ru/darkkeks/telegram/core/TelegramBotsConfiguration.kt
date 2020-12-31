package ru.darkkeks.telegram.core

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import ru.darkkeks.telegram.core.api.Telegram
import ru.darkkeks.telegram.core.api.TelegramApi
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService


@Configuration
@ComponentScan
@PropertySource("classpath:core-properties.yml", factory = YamlPropertyLoaderFactory::class)
class TelegramBotsConfiguration {

    @Bean
    fun scheduledExecutorService(): ScheduledExecutorService = Executors.newScheduledThreadPool(4)

    @Bean
    fun okHttpClient(): OkHttpClient {
        val logger = createLogger<Retrofit>()
        val interceptor = HttpLoggingInterceptor { message -> logger.info(message) }

        interceptor.level = HttpLoggingInterceptor.Level.BASIC
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }

    @Bean
    fun telegramApi(
        okHttpClient: OkHttpClient,
        @Value("\${telegram.base_url}") baseUrl: String,
        @Value("\${telegram.token}") telegramToken: String
    ): TelegramApi {
        return Retrofit.Builder()
            .baseUrl("$baseUrl$telegramToken/")
            .addConverterFactory(JacksonConverterFactory.create(objectMapper))
            .client(okHttpClient)
            .build()
            .create(TelegramApi::class.java)
    }

}

