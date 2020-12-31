package ru.darkkeks.telegram.lifestats

import ru.darkkeks.telegram.core.api.CallbackQuery
import ru.darkkeks.telegram.core.api.Message
import ru.darkkeks.telegram.core.api.Update
import ru.darkkeks.telegram.core.api.UpdateHandler
import ru.darkkeks.telegram.core.createLogger
import ru.darkkeks.telegram.core.toJsonPretty

class RoutingMessageHandler(
    private val userDataProvider: UserDataProvider,
    private val handlers: List<Handler>
) : UpdateHandler {

    private val logger = createLogger<RoutingMessageHandler>()

    override fun handle(update: Update) {
        when {
            update.message != null -> {
                val message = update.message!!
                val chat = message.chat
                val user = message.from ?: return  // TODO
                val userData = userDataProvider.findUser(user.id, chat.id) ?: return  // TODO
                val context = MessageContext(message, userData)
                val handler = handlers
                    .filterIsInstance<MessageHandler>()
                    .firstOrNull { it.matches(context) }
                if (handler != null) {
                    handler.handle(context)
                } else {
                    logger.warn("No matching handler for update! {}", toJsonPretty(update))
                }
            }
        }
    }
}

class UserData(
    val id: Int,
    val chatId: Long,
    val state: String
)

interface UserDataProvider {
    fun findUser(id: Int, chatId: Long): UserData?
}

class MessageContext(
    val message: Message,
    val userData: UserData
)

class CallbackContext(
    val message: Message,
    val callbackQuery: CallbackQuery,
    val userData: UserData
)

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

