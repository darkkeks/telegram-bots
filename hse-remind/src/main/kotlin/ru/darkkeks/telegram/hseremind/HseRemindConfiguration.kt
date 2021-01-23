package ru.darkkeks.telegram.hseremind

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.youtube.YouTube
import com.google.api.services.youtube.YouTubeRequestInitializer
import okhttp3.OkHttpClient
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
import ru.darkkeks.telegram.core.objectMapper
import ru.darkkeks.telegram.hseremind.ruz.RuzApi
import java.util.concurrent.ScheduledExecutorService


@Configuration
@Import(TelegramBotsConfiguration::class)
class HseRemindConfiguration {

    @Bean
    fun telegramFiles(
        okHttpClient: OkHttpClient,
        @Value("\${telegram.file_url}") baseUrl: String,
        @Value("\${telegram.token}") telegramToken: String
    ): TelegramFiles {
        return Retrofit.Builder()
            .baseUrl("$baseUrl$telegramToken/")
            .addConverterFactory(JacksonConverterFactory.create(objectMapper))
            .client(okHttpClient)
            .build()
            .create(TelegramFiles::class.java)
    }

    @Bean
    fun ruzApi(
        okHttpClient: OkHttpClient,
        @Value("\${ruz.base_url}") baseUrl: String
    ): RuzApi {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(
                JacksonConverterFactory.create(
                    ObjectMapper()
                        .registerModule(KotlinModule())
                        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                )
            )
            .client(okHttpClient)
            .build()
            .create(RuzApi::class.java)
    }

    @Bean
    fun youtube(@Value("\${youtube.key}") key: String): YouTube {
        val jacksonFactory = JacksonFactory.getDefaultInstance()
        val httpTransport = GoogleNetHttpTransport.newTrustedTransport()
        return YouTube.Builder(httpTransport, jacksonFactory, null)
            .setYouTubeRequestInitializer(YouTubeRequestInitializer(key))
            .setApplicationName(this::class.simpleName)
            .build()
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
