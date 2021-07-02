package ru.darkkeks.telegram.lifestats.util

import ru.darkkeks.telegram.core.handle_wip.ButtonState
import ru.darkkeks.telegram.lifestats.CallbackButtonContext
import ru.darkkeks.telegram.lifestats.CallbackContext
import ru.darkkeks.telegram.lifestats.CallbackHandler
import ru.darkkeks.telegram.lifestats.Handler
import ru.darkkeks.telegram.lifestats.MessageContext
import ru.darkkeks.telegram.lifestats.MessageHandler
import kotlin.reflect.KClass

fun handlerList(state: String? = null, block: HandlerListBuilder.() -> Unit): List<Handler> {
    return HandlerListBuilder(state).apply { block() }.build()
}

fun HandlerListBuilder.withState(state: String, block: HandlerListBuilder.() -> Unit) {
    val subHandlers = handlerList(state) {
        block()
    }
    subHandlers.forEach { add(it) }
}


// TODO Сейчас невозможно добавить настоящий fallback (который заматчит несуществующий state)
class HandlerListBuilder(
    private val state: String?,
) {
    private val handlers = mutableListOf<Handler>()

    fun build(): List<Handler> = handlers

    fun add(handler: Handler) = handlers.add(handler)

    fun command(command: String, block: (MessageContext) -> Unit) {
        val handler = object : MessageHandler {
            override fun matches(context: MessageContext): Boolean {
                val text = context.message.text ?: return false
                return context.user.state == state
                        && text.trim().startsWith("/$command")
            }

            override fun handle(context: MessageContext) = block(context)
        }
        handlers.add(handler)
    }

    fun <T : ButtonState> callback(type: KClass<T>, block: (CallbackButtonContext<T>) -> Unit) {
        val handler = object : CallbackHandler {
            override fun matches(context: CallbackContext): Boolean {
                return context.user.state == state
                        && context is CallbackButtonContext<*>
                        && type.isInstance(context.state)
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

    /**
     * Обработчик, который матчит любые события. Должен располагаться после всех осмысленных обработчиков.
     */
    fun fallback(block: (MessageContext) -> Unit) {
        val handler = object : MessageHandler {
            override fun matches(context: MessageContext) = context.user.state == state
            override fun handle(context: MessageContext) = block(context)
        }
        handlers.add(handler)
    }

    /**
     * Обработчик, который матчит любые события. Должен располагаться после всех осмысленных обработчиков.
     */
    fun fallbackCallback(block: (CallbackContext) -> Unit) {
        val handler = object : CallbackHandler {
            override fun matches(context: CallbackContext) = context.user.state == state
            override fun handle(context: CallbackContext) = block(context)
        }
        handlers.add(handler)
    }
}
