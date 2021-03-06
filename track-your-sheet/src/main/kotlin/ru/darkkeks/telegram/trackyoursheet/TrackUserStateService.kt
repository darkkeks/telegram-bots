package ru.darkkeks.telegram.trackyoursheet

import org.springframework.stereotype.Component
import ru.darkkeks.telegram.core.api.Chat
import ru.darkkeks.telegram.core.api.User
import ru.darkkeks.telegram.core.handle.UserStateService

@Component
class TrackUserStateService : UserStateService {
    val data: MutableMap<Pair<Int, Long>, TrackUserState> = mutableMapOf()

    override fun get(user: User, chat: Chat): TrackUserState? {
        return data[user.id to chat.id] ?: TrackUserState(user.id, chat.id)
    }

    fun persist(state: TrackUserState) {
        data[state.userId to state.chatId] = state
    }
}
