package ru.darkkeks.telegram.lifestats

import ru.darkkeks.telegram.core.handle_wip.ButtonState
import kotlin.reflect.KClass

fun handlerList(block: HandlerListBuilder.() -> Unit): List<Handler> {
    return HandlerListBuilder().apply { block() }.build()
}

class HandlerListBuilder {

    private val handlers = mutableListOf<Handler>()

    fun build(): List<Handler> = handlers

    fun command(command: String, block: (MessageContext) -> Unit) {
        val handler = object : MessageHandler {
            override fun matches(context: MessageContext): Boolean {
                val text = context.message.text ?: return false
                return text.startsWith("/$command")
            }

            override fun handle(context: MessageContext) = block(context)
        }
        handlers.add(handler)
    }

    fun <T : ButtonState> callback(type: KClass<T>, block: (CallbackButtonContext<T>) -> Unit) {
        val handler = object : CallbackHandler {
            override fun matches(context: CallbackContext): Boolean {
                return context is CallbackButtonContext<*> && type.isInstance(context.state)
            }

            override fun handle(context: CallbackContext) {
                @Suppress("UNCHECKED_CAST") // проверено в matches
                block(context as CallbackButtonContext<T>)
            }
        }
        handlers.add(handler)
    }

    inline fun <reified T : ButtonState> callback(noinline block: (CallbackButtonContext<T>) -> Unit) =
        callback(T::class, block)

    fun fallback(block: (MessageContext) -> Unit) {
        val handler = object : MessageHandler {
            override fun matches(context: MessageContext) = true
            override fun handle(context: MessageContext) = block(context)
        }
        handlers.add(handler)
    }

}
