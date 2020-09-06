package ru.darkkeks.telegram.core.handle_wip

import ru.darkkeks.telegram.core.serialize.BufferSerializable

abstract class UserState

abstract class MessageState : BufferSerializable

abstract class ButtonState : BufferSerializable

abstract class TextButton(val text: String) : ButtonState()
