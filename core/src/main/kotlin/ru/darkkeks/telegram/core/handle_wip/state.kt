package ru.darkkeks.telegram.core.handle_wip

import ru.darkkeks.telegram.core.serialize.BufferSerializable
import ru.darkkeks.telegram.core.serialize.ButtonBuffer

interface UserState

interface MessageState : BufferSerializable

abstract class ButtonState(
    private val serializer: ButtonBuffer.() -> Unit
) : BufferSerializable {
    constructor() : this({})
    override fun write(buffer: ButtonBuffer) = buffer.serializer()
}

data class ButtonWithText(
    val text: String,
    val state: ButtonState,
)

fun ButtonState.serialize(id: Byte): String {
    return ButtonBuffer().also {
        it.pushByte(id)
        write(it)
    }.serialize()
}
