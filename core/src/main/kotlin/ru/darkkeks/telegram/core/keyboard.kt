package ru.darkkeks.telegram.core

import ru.darkkeks.telegram.core.handle_wip.ButtonState
import ru.darkkeks.telegram.core.handle_wip.ButtonWithText

fun buildKeyboard(block: KeyboardBuilder.() -> Unit): List<List<ButtonWithText>> {
    return KeyboardBuilder().also(block).build()
}

class KeyboardBuilder {
    private val result: MutableList<MutableList<ButtonWithText>> = mutableListOf()

    fun row(text: String, state: ButtonState) = row(ButtonWithText(text, state))

    fun add(text: String, state: ButtonState) = add(ButtonWithText(text, state))

    fun row(vararg buttons: ButtonWithText) {
        row()
        buttons.forEach { add(it) }
    }

    fun row() {
        if (result.isNotEmpty() && result.last().isNotEmpty()) {
            result.add(mutableListOf())
        }
    }

    fun add(vararg buttons: ButtonWithText) {
        if (result.isEmpty()) {
            result.add(mutableListOf())
        }
        buttons.forEach {
            result.last().add(it)
        }
    }

    fun build(): List<List<ButtonWithText>> {
        return result
    }
}
