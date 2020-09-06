package ru.darkkeks.telegram.core.handle_wip

import ru.darkkeks.telegram.core.api.CallbackQuery
import ru.darkkeks.telegram.core.api.Message
import kotlin.reflect.KClass
import kotlin.reflect.cast

data class MessageContext<U : UserState>(
        val userState: U,
        val message: Message
) {
    fun <T : U> castDown(klass: KClass<T>): MessageContext<T> {
        return MessageContext(klass.cast(userState), message)
    }
}

data class CallbackContext<U : UserState, M : MessageState, B : ButtonState>(
        val userState: U,
        val messageState: M,
        val buttonState: B,
        val callbackQuery: CallbackQuery,
        val message: Message
) {
    fun <TU : U, TM : M, TB : B> castDown(
            userClass: KClass<TU>,
            messageClass: KClass<TM>,
            buttonClass: KClass<TB>
    ): CallbackContext<TU, TM, TB> {
        return CallbackContext(
                userClass.cast(userState),
                messageClass.cast(messageState),
                buttonClass.cast(buttonState),
                callbackQuery,
                message
        )
    }
}

interface Handler

interface MessageHandler : Handler {
    fun matches(context: MessageContext<UserState>): Boolean
    fun handle(context: MessageContext<UserState>)
}

interface CallbackHandler : Handler {
    fun matches(context: CallbackContext<UserState, MessageState, ButtonState>): Boolean
    fun handle(context: CallbackContext<UserState, MessageState, ButtonState>)
}

abstract class AbstractMessageHandler<U : UserState> : MessageHandler {
    override fun matches(context: MessageContext<UserState>): Boolean {
        return getUserStateClass().isInstance(context.userState)
    }

    final override fun handle(context: MessageContext<UserState>) {
        require(getUserStateClass().isInstance(context.userState)) {
            "User state has to be instance of ${getUserStateClass()}"
        }

        handleTyped(context.castDown(getUserStateClass()))
    }

    abstract fun getUserStateClass(): KClass<U>

    abstract fun handleTyped(context: MessageContext<U>)
}

abstract class AbstractCallbackHandler<U : UserState, M : MessageState, B : ButtonState> : CallbackHandler {
    override fun matches(context: CallbackContext<UserState, MessageState, ButtonState>): Boolean {
        return checkType(context.userState, context.messageState, context.buttonState)
    }

    override fun handle(context: CallbackContext<UserState, MessageState, ButtonState>) {
        require(checkType(context.userState, context.messageState, context.buttonState)) {
            "State classes do not match"
        }

        handleTyped(context.castDown(getUserStateClass(), getMessageStateClass(), getButtonStateClass()))
    }

    private fun checkType(userState: UserState, messageState: MessageState, button: ButtonState): Boolean {
        return getUserStateClass().isInstance(userState) &&
                getMessageStateClass().isInstance(messageState) &&
                getButtonStateClass().isInstance(button)
    }

    abstract fun getUserStateClass(): KClass<U>
    abstract fun getMessageStateClass(): KClass<M>
    abstract fun getButtonStateClass(): KClass<B>

    abstract fun handleTyped(context: CallbackContext<U, M, B>)
}

inline fun <reified U : UserState, reified M : MessageState, reified B : ButtonState> abstractCallbackHandler(
        crossinline block: CallbackContext<U, M, B>.() -> Unit
): CallbackHandler {
    return object : AbstractCallbackHandler<U, M, B>() {
        override fun getUserStateClass() = U::class
        override fun getMessageStateClass() = M::class
        override fun getButtonStateClass() = B::class
        override fun handleTyped(context: CallbackContext<U, M, B>) = block(context)
    }
}
