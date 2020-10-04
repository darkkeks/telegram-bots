package ru.darkkeks.telegram.core.api

class TelegramResponse<T>(
        val ok: Boolean,
        private val result: T?,
        private val description: String?,
        private val errorCode: Int?,
        private val parameters: ResponseParameters?
) {
    fun checked(): T {
        checkError()
        return result!!
    }

    private fun checkError() {
        if (ok) return
        when {
            description != null && errorCode != null ->
                throw TelegramClientException("Telegram api call failed with error code $errorCode and description '$description'")
            description != null ->
                throw TelegramClientException("Telegram api call failed with description '$description'")
            errorCode != null ->
                throw TelegramClientException("Telegram api call failed with errorCode $errorCode")
        }
    }
}
