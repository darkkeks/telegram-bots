package ru.darkkeks.telegram.core

import ru.darkkeks.telegram.core.handle_wip.ButtonState
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

    fun command(command: String, block: (CommandContext) -> Unit) {
        val handler = object : MessageHandler {
            override fun matches(context: MessageContext): Boolean {
                val text = context.message.text ?: return false
                return checkState(context) && text.trim().startsWith("/$command")
            }

            override fun handle(context: MessageContext) {
                val text = context.message.text!!
                val parts = text.split("""\s""".toRegex())
                val args = parts.drop(1)
                block(CommandContext(context, args))
            }
        }
        handlers.add(handler)
    }

    fun document(block: (MessageContext) -> Unit) {
        val handler = object : MessageHandler {
            override fun matches(context: MessageContext): Boolean {
                return checkState(context) && context.message.document != null
            }

            override fun handle(context: MessageContext) = block(context)
        }
        handlers.add(handler)
    }

    fun <T : ButtonState> callback(type: KClass<T>, block: (CallbackButtonContext<T>) -> Unit) {
        val handler = object : CallbackHandler {
            override fun matches(context: CallbackContext): Boolean {
                return checkState(context)
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
            override fun matches(context: MessageContext) = checkState(context)
            override fun handle(context: MessageContext) = block(context)
        }
        handlers.add(handler)
    }

    /**
     * Обработчик, который матчит любые события. Должен располагаться после всех осмысленных обработчиков.
     */
    fun fallbackCallback(block: (CallbackContext) -> Unit) {
        val handler = object : CallbackHandler {
            override fun matches(context: CallbackContext) = checkState(context)
            override fun handle(context: CallbackContext) = block(context)
        }
        handlers.add(handler)
    }

    private fun checkState(context: Context): Boolean {
        return state == null || context.user.state == state
    }
}
