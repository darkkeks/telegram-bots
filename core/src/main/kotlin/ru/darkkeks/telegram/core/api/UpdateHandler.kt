package ru.darkkeks.telegram.core.api

interface UpdateHandler {
    fun handle(update: Update)
}
