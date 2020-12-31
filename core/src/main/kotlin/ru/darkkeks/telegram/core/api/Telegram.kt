package ru.darkkeks.telegram.core.api

import okhttp3.MultipartBody
import org.springframework.stereotype.Component

@Component
class Telegram(
    val telegramApi: TelegramApi
) {

    fun getMe() = telegramApi.getMe().executeChecked()

    fun sendMessage(
        chatId: Long,
        text: String,
        parseMode: ParseMode? = null,
        disableWebPagePreview: Boolean? = null,
        disableNotification: Boolean? = null,
        replyToMessageId: Int? = null,
        replyMarkup: Markup? = null
    ) = telegramApi.sendMessage(
        SendMessageRequest(
            chatId = chatId,
            text = text,
            parseMode = parseMode,
            disableNotification = disableNotification,
            disableWebPagePreview = disableWebPagePreview,
            replyToMessageId = replyToMessageId,
            replyMarkup = replyMarkup
        )
    ).executeChecked()

    fun sendChatAction(
        chatId: Long,
        action: ChatAction
    ) = telegramApi.sendChatAction(
        SendChatActionRequest(
            chatId = chatId,
            action = action
        )
    ).executeChecked()

    fun answerCallbackQuery(
        callbackQueryId: String,
        text: String? = null,
        showAlert: Boolean? = null,
        url: String? = null,
        cacheTime: Int? = null
    ) = telegramApi.answerCallbackQuery(
        AnswerCallbackQueryRequest(
            callbackQueryId = callbackQueryId,
            text = text,
            showAlert = showAlert,
            url = url,
            cacheTime = cacheTime
        )
    ).executeChecked()

    fun editMessageText(
        chatId: Long? = null,
        messageId: Int? = null,
        text: String,
        inlineMessageId: String? = null,
        parseMode: ParseMode? = null,
        disableWebPagePreview: Boolean? = null,
        replyMarkup: InlineKeyboardMarkup? = null
    ) = telegramApi.editMessageText(
        EditMessageTextRequest(
            chatId = chatId,
            messageId = messageId,
            text = text,
            inlineMessageId = inlineMessageId,
            parseMode = parseMode,
            disableWebPagePreview = disableWebPagePreview,
            replyMarkup = replyMarkup
        )
    ).executeChecked()

    fun getUpdates(
        offset: Int? = null,
        limit: Int? = null,
        timeout: Int? = null,
        allowedUpdates: List<String>? = null
    ) = telegramApi.getUpdates(
        GetUpdatesRequest(
            offset = offset,
            limit = limit,
            timeout = timeout,
            allowedUpdates = allowedUpdates
        )
    ).executeChecked()

    fun getFile(
        fileId: String
    ) = telegramApi.getFile(
        GetFileRequest(
            fileId = fileId
        )
    ).executeChecked()

    fun sendDocument(
        document: MultipartBody.Part,
        chatId: Long,
        thumb: String? = null,
        caption: String? = null,
        parseMode: ParseMode? = null,
        disableNotification: Boolean? = null,
        replyToMessageId: Boolean? = null,
        replyMarkup: Markup? = null
    ) = telegramApi.sendDocument(
        document, chatId, thumb, caption, parseMode, disableNotification, replyToMessageId, replyMarkup
    ).executeChecked()
}
