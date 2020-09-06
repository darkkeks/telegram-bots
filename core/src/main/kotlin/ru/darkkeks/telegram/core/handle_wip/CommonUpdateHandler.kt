package ru.darkkeks.telegram.core.handle_wip

import ru.darkkeks.telegram.core.api.Update
import ru.darkkeks.telegram.core.api.UpdateHandler
import ru.darkkeks.telegram.core.createLogger
import ru.darkkeks.telegram.core.serialize.ButtonBuffer
import java.util.*

class CommonUpdateHandler(
        private val userStateService: UserStateService,
        private val actionRouter: ActionRouter
) : UpdateHandler {
    override fun handle(update: Update) {
        when {
            update.message != null -> {
                val message = update.message

                val chat = message.chat
                val user = message.from ?: return  // channel messages

                val userState = userStateService.get(user, chat) ?: return

                actionRouter.handle(MessageContext(userState, message))
            }
            update.callbackQuery != null -> {
                val callbackQuery = update.callbackQuery
                val message = callbackQuery.message ?: return  // inline request button

                val chat = message.chat
                val user = callbackQuery.from
                val data = callbackQuery.data ?: return  // game

                val userState = userStateService.get(user, chat) ?: return

                val buffer = try {
                    ButtonBuffer(data)
                } catch (e: IllegalArgumentException) {
                    logger.info("Could not deserialize message data {}",
                            Base64.getEncoder().encode(data.toByteArray()), e)
                    return
                }

//                actionRouter.handle(userState, callbackQuery, buffer)
            }
        }
    }

    companion object {
        private val logger = createLogger<CommonUpdateHandler>()
    }
}
