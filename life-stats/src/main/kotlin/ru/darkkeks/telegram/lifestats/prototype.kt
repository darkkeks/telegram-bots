package ru.darkkeks.telegram.lifestats

import org.springframework.stereotype.Component
import ru.darkkeks.telegram.core.api.CallbackQuery
import ru.darkkeks.telegram.core.api.ChatType
import ru.darkkeks.telegram.core.api.InlineKeyboardButton
import ru.darkkeks.telegram.core.api.InlineKeyboardMarkup
import ru.darkkeks.telegram.core.api.Message
import ru.darkkeks.telegram.core.api.Telegram
import ru.darkkeks.telegram.core.api.Update
import ru.darkkeks.telegram.core.api.UpdateHandler
import ru.darkkeks.telegram.core.createLogger
import ru.darkkeks.telegram.core.handle_wip.ButtonState
import ru.darkkeks.telegram.core.handle_wip.ButtonWithText
import ru.darkkeks.telegram.core.handle_wip.serialize
import ru.darkkeks.telegram.core.serialize.ButtonBuffer
import ru.darkkeks.telegram.core.serialize.Registry
import ru.darkkeks.telegram.core.toJsonPretty
import ru.darkkeks.telegram.lifestats.service.UserService.Companion.MAIN_STATE

class RoutingMessageHandler(
    private val telegram: Telegram,
    private val userDataProvider: UserDataProvider,
    private val buttonConverter: ButtonConverter,
    private val handlers: List<Handler>,
) : UpdateHandler {

    private val logger = createLogger<RoutingMessageHandler>()

    override fun handle(update: Update) {
        logger.info("Received update: {}", toJsonPretty(update))
        when {
            update.message != null -> {
                val message = update.message!!
                val chat = message.chat
                val user = message.from ?: return  // TODO
                if (message.chat.type != ChatType.PRIVATE) {
                    telegram.sendMessage(message.chat.id, "Only private chats are currently supported.")
                    return
                }
                val userData = userDataProvider.findUser(user.id, chat.id) ?: return  // TODO
                val context = MessageContext(message, userData)
                val handler = handlers
                    .filterIsInstance<MessageHandler>()
                    .firstOrNull { it.matches(context) }
                if (handler != null) {
                    handler.handle(context)
                } else {
                    logger.error("No matching handler for update! {}", toJsonPretty(update))
                }
            }
            update.callbackQuery != null -> {
                val callbackQuery = update.callbackQuery!!
                val message = callbackQuery.message ?: return  // TODO
                val chat = message.chat
                if (message.chat.type != ChatType.PRIVATE) {
                    telegram.sendMessage(message.chat.id, "Only private chats are currently supported.")
                    return
                }
                val user = callbackQuery.from
                val userData = userDataProvider.findUser(user.id, chat.id) ?: return  // TODO
                val data = callbackQuery.data ?: return  // TODO
                val state = buttonConverter.deserialize(data)
                val context = if (state != null) {
                    CallbackButtonContext(message, userData, callbackQuery, state)
                } else {
                    CallbackContext(message, userData, callbackQuery)
                }
                val handler = handlers
                    .filterIsInstance<CallbackHandler>()
                    .firstOrNull { it.matches(context) }
                if (handler != null) {
                    handler.handle(context)
                } else {
                    logger.error("No matching handler for update! {}", toJsonPretty(update))
                }
                if (context.answer()) {
                    telegram.answerCallbackQuery(callbackQuery.id)
                }
            }
        }
    }
}

@Component
class ButtonConverter(
    private val buttonStateRegistry: Registry<ButtonState>,
) {
    private val logger = createLogger<ButtonConverter>()

    fun serialize(keyboard: List<List<ButtonWithText>>): InlineKeyboardMarkup {
        return InlineKeyboardMarkup(
            buildList {
                for (row in keyboard) {
                    add(buildList {
                        for (button in row) {
                            val id = buttonStateRegistry.getId(button.state)
                            if (id == null) {
                                logger.warn("Button ${button.state::class} is not registered!")
                            }
                            val buffer = button.state.serialize(id ?: -1)
                            add(
                                InlineKeyboardButton(
                                    text = button.text,
                                    callbackData = buffer,
                                )
                            )
                        }
                    })
                }
            }
        )
    }

    fun deserialize(data: String): ButtonState? {
        val buffer = ButtonBuffer(data)
        return buttonStateRegistry.get(buffer)
    }
}

typealias StateData = Map<String, Any?>

/**
 * TODO Надо придумать что-то с дефолтным состоянием. Этот класс задумывался как класс, который не зависит от
 * приложения. Но при этом у него есть функции, которым хотелось бы знать дефолтное состояние (например метод сброса
 * состояния в дефолтное). Например, можно сделать дефолтное состояние null, с пустой мапой
 */
data class UserData(
    val id: Long,
    val chatId: Long,
    val state: String = MAIN_STATE,
    val stateData: StateData,
)

fun UserData.resetState() = setState(MAIN_STATE)

fun UserData.setState(state: String): UserData {
    return copy(state = state, stateData = mapOf())
}

fun UserData.setState(state: String, data: Map<String, Any?>): UserData {
    return copy(state = state, stateData = data)
}

fun UserData.updateState(data: Map<String, Any?>): UserData {
    return copy(stateData = stateData + data)
}

fun UserData.updateState(state: String, data: Map<String, Any?>): UserData {
    return copy(state = state, stateData = stateData + data)
}

interface UserDataProvider {
    fun findUser(id: Long, chatId: Long): UserData?
}

open class Context(
    val message: Message,
    val user: UserData,
)

class MessageContext(
    message: Message,
    userData: UserData,
) : Context(message, userData)

open class CallbackContext(
    message: Message,
    userData: UserData,
    val callbackQuery: CallbackQuery,
    private var answered: Boolean = false,
) : Context(message, userData) {
    private val canAnswer
        get() = !answered

    fun answer(): Boolean = canAnswer.also {
        answered = true
    }
}

class CallbackButtonContext<out T : ButtonState>(
    message: Message,
    userData: UserData,
    callbackQuery: CallbackQuery,
    val state: T,
) : CallbackContext(message, userData, callbackQuery)

interface HandlerFactory {
    fun handlers(): List<Handler>
}

interface Handler

interface MessageHandler : Handler {
    fun matches(context: MessageContext): Boolean
    fun handle(context: MessageContext)
}

interface CallbackHandler : Handler {
    fun matches(context: CallbackContext): Boolean
    fun handle(context: CallbackContext)
}

