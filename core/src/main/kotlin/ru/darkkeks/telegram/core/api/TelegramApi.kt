package ru.darkkeks.telegram.core.api

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Multipart
import retrofit2.http.Part
import retrofit2.http.Query

interface TelegramApi {

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
    ): Call<TelegramResponse<Message>>

    @POST("getMe")
    fun getMe(): Call<TelegramResponse<User>>

    @POST("sendMessage")
    fun sendMessage(@Body request: SendMessageRequest): Call<TelegramResponse<Message>>

    @POST("sendChatAction")
    fun sendChatAction(@Body request: SendChatActionRequest): Call<TelegramResponse<Boolean>>

    @POST("answerCallbackQuery")
    fun answerCallbackQuery(@Body request: AnswerCallbackQueryRequest): Call<TelegramResponse<Boolean>>

    @POST("editMessageText")
    fun editMessageText(@Body request: EditMessageTextRequest): Call<TelegramResponse<Void>>

    @POST("getUpdates")
    fun getUpdates(@Body request: GetUpdatesRequest): Call<TelegramResponse<List<Update>>>

    @POST("getFile")
    fun getFile(@Body request: GetFileRequest): Call<TelegramResponse<File>>
}
