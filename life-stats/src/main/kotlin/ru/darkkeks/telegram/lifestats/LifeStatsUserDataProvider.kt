package ru.darkkeks.telegram.lifestats

import org.springframework.stereotype.Component

@Component
class LifeStatsUserDataProvider : UserDataProvider {
    override fun findUser(id: Int, chatId: Long): UserData {
        return UserData(id, chatId, "")
    }
}
