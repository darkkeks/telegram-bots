package ru.darkkeks.telegram.core.api

import com.fasterxml.jackson.core.type.TypeReference
import retrofit2.Call
import ru.darkkeks.telegram.core.createLogger
import ru.darkkeks.telegram.core.objectMapper
import java.io.IOException

fun <T> Call<TelegramResponse<T>>.executeTelegram(): TelegramResponse<T> {
    val response = try {
        execute()
    } catch (e: IOException) {
        throw TelegramClientException("Telegram api call failed", e)
    }

    return if (response.isSuccessful) {
        response.body() ?: throw TelegramClientException("No response body " +
                "(response code ${response.code()}, ${response.message()})")
    } else {
        val errorBody = response.errorBody()
        errorBody ?: throw TelegramClientException("No error response body " +
                "(response code ${response.code()}, ${response.message()})")

        objectMapper.readValue(errorBody.byteStream(), object : TypeReference<TelegramResponse<T>>() {})
    }
}


fun <T> Call<TelegramResponse<T>>.executeChecked(): T {
    return try {
        executeTelegram().checked()
    } catch (e: TelegramClientException) {
        val logger = createLogger<TelegramResponse<T>>()
        logger.error("Telegram request error", e)

        throw e
    }
}

fun createInlineButton(
        text: String,
        url: String? = null,
        loginUrl: LoginUrl? = null,
        callbackData: String? = null,
        switchInlineQuery: String? = null,
        switchInlineQueryCurrentChat: String? = null,
        callbackGame: CallbackGame? = null,
        pay: Boolean? = null
) = InlineKeyboardButton(
        text = text,
        url = url,
        loginUrl = loginUrl,
        callbackData = callbackData,
        switchInlineQuery = switchInlineQuery,
        switchInlineQueryCurrentChat = switchInlineQueryCurrentChat,
        callbackGame = callbackGame,
        pay = pay
)
