package ru.darkkeks.telegram.core.handle_wip

import ru.darkkeks.telegram.core.serialize.Registry

class ActionRouter(
    private val messageStateRegistry: Registry<MessageState>,
    private val buttonStateRegistry: Registry<ButtonState>,
    private val handlers: List<Handler>
) {
    fun handle(context: MessageContext<UserState>) {
        val handler = handlers
                .filterIsInstance<MessageHandler>()
                .firstOrNull { it.matches(context) }

        handler ?: return  // TODO

        handler.handle(context)
    }

    fun handle(context: CallbackContext<UserState, MessageState, ButtonState>) {
//        val messageState = messageStateRegistry.get(context.) ?: return // TODO
//        val buttonState = buttonStateRegistry.get(data) ?: return  // TODO
//
//        val context = CallbackContext(userState, messageState, buttonState, callbackQuery, message)
//
//        val handler = handlers
//                .filterIsInstance<CallbackHandler>()
//                .firstOrNull { it.matches(userState, messageState, buttonState) }
//
//        handler ?: return  // TODO
//
//        handler.handle(userState, messageState, buttonState)
    }
}
