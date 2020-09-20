package ru.darkkeks.telegram.core.api

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface Telegram {

    @GET("getMe")
    fun getMe(): Call<TelegramResponse<User>>

    @GET("sendMessage")
    fun sendMessage(
            @Query("chat_id") chatId: Long,
            @Query("text") text: String,
            @Query("parse_mode") parseMode: ParseMode? = null,
            @Query("disable_web_page_preview") disableWebPagePreview: Boolean? = null,
            @Query("disable_notification") disableNotification: Boolean? = null,
            @Query("reply_to_message_id") replyToMessageId: Boolean? = null,
            @Query("reply_markup") replyMarkup: Markup? = null
    ) : Call<TelegramResponse<Message>>

    @Multipart
    @POST("sendDocument")
    fun sendDocument(
            @Part document: MultipartBody.Part,
            @Query("chat_id") chatId: Long,
            @Query("thumb") thumb: String? = null,
            @Query("caption") caption: String? = null,
            @Query("parse_mode") parseMode: ParseMode? = null,
            @Query("disable_notification") disableNotification: Boolean? = null,
            @Query("reply_to_message_id") replyToMessageId: Boolean? = null,
            @Query("reply_markup") replyMarkup: Markup? = null
    ) : Call<TelegramResponse<Message>>

    @GET("sendChatAction")
    fun sendChatAction(
            @Query("chat_id") chatId: Int,
            @Query("action") action: ChatAction
    ) : Call<TelegramResponse<Boolean>>

    @GET("answerCallbackQuery")
    fun answerCallbackQuery(
            @Query("callback_query_id") callbackQueryId: String,
            @Query("text") text: String? = null,
            @Query("show_alert") showAlert: Boolean? = null,
            @Query("url") url: String? = null,
            @Query("cache_time") cacheTime: Int? = null
    ) : Call<TelegramResponse<Boolean>>

    @GET("editMessageText")
    fun editMessageText(
            @Query("chat_id") chatId: Long? = null,
            @Query("message_id") messageId: Int? = null,
            @Query("text") text: String,
            @Query("inline_message_id") inlineMessageId: String? = null,
            @Query("parse_mode") parseMode: ParseMode? = null,
            @Query("disable_web_page_preview") disableWebPagePreview: Boolean? = null,
            @Query("reply_markup") replyMarkup: InlineKeyboardMarkup? = null
    ) : Call<Void>

    @GET("getUpdates")
    fun getUpdates(
            @Query("offset") offset: Int? = null,
            @Query("limit") limit: Int? = null,
            @Query("timeout") timeout: Int? = null,
            @Query("allowed_updates") allowedUpdates: List<String>? = null
    ) : Call<TelegramResponse<List<Update>>>

    @GET("getFile")
    fun getFile(
            @Query("file_id") fileId: String
    ) : Call<TelegramResponse<File>>

}
