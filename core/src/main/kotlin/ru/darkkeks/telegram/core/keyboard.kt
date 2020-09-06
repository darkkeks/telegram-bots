package ru.darkkeks.telegram.core

import ru.darkkeks.telegram.core.handle_wip.ButtonState

fun buildKeyboard(block: KeyboardBuilder.() -> Unit): List<List<ButtonState>> {
    return KeyboardBuilder().also(block).build()
}

class KeyboardBuilder {
    private val result: MutableList<MutableList<ButtonState>> = mutableListOf()

    fun row() {
        if (result.isNotEmpty() && result.last().isNotEmpty()) {
            result.add(mutableListOf())
        }
    }

    fun add(vararg buttons: ButtonState) {
        if (result.isEmpty()) {
            result.add(mutableListOf())
        }
        buttons.forEach {
            result.last().add(it)
        }
    }

    fun build(): List<List<ButtonState>> {
        return result
    }
}
