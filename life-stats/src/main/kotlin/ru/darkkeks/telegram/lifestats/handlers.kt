package ru.darkkeks.telegram.lifestats

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

    fun fallback(block: (MessageContext) -> Unit) {
        val handler = object : MessageHandler {
            override fun matches(context: MessageContext) = true
            override fun handle(context: MessageContext) = block(context)
        }
        handlers.add(handler)
    }

}

