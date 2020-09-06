package ru.darkkeks.telegram.core.handle_wip

import ru.darkkeks.telegram.core.api.Chat
import ru.darkkeks.telegram.core.api.User

interface UserStateService {
    fun get(user: User, chat: Chat): UserState?
}
