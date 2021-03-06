package ru.darkkeks.telegram.trackyoursheet

import org.springframework.stereotype.Component
import ru.darkkeks.telegram.core.api.InlineKeyboardButton
import ru.darkkeks.telegram.core.api.InlineKeyboardMarkup
import ru.darkkeks.telegram.core.api.TelegramApi
import ru.darkkeks.telegram.core.handle.ButtonState
import ru.darkkeks.telegram.core.handle.MessageState
import ru.darkkeks.telegram.core.handle.TextButton
import ru.darkkeks.telegram.core.serialize.ButtonBuffer
import ru.darkkeks.telegram.core.serialize.Registry

@Component
class StatefulMessagesService(
        private val telegramApi: TelegramApi,
        private val messageStateRegistry: Registry<MessageState>,
        private val buttonStateRegistry: Registry<ButtonState>
) {
    fun editStatefulMessage(
            chatId: Long,
            messageId: Int,
            render: StatefulMessageRender
    ) {
        val markup = render.keyboard?.let {
            InlineKeyboardMarkup(it.map { row ->
                row.map { buttonState ->
                    InlineKeyboardButton(
                            text = getButtonText(buttonState),
                            callbackData = serialize(render.state, buttonState).serialize()
                    )
                }
            })
        }
        telegramApi.editMessageText(chatId, messageId, render.text, replyMarkup = markup)
    }

    fun getButtonText(buttonState: ButtonState) = when (buttonState) {
        is TextButton -> buttonState.text
        else -> "🙈 Invalid button 🙈"
    }

    fun serialize(messageState: MessageState, buttonState: ButtonState): ButtonBuffer {
        val messageStateId = messageStateRegistry.getId(messageState)
        val buttonStateId = buttonStateRegistry.getId(buttonState)
        require(messageStateId != null) { "Message state ${messageState::class} is not registered" }
        require(buttonStateId != null) { "Button state ${buttonState::class} is not registered" }

        val result = ButtonBuffer()
        result.pushByte(messageStateId)
        messageState.write(result)
        result.pushByte(buttonStateId)
        messageState.write(result)
        return result
    }
}

class StatefulMessageRender(
        val state: MessageState,
        val text: String,
        val keyboard: List<List<ButtonState>>?
)
