package ru.darkkeks.telegram.hseremind

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Component
import ru.darkkeks.telegram.core.MAIN_STATE
import ru.darkkeks.telegram.core.UserData
import ru.darkkeks.telegram.core.UserDataProvider
import ru.darkkeks.telegram.core.unwrap

@Component
interface UserRepository : CrudRepository<User, Long>

data class User(
    val id: Long,
    val spec: String
)

@Component
class UserService : UserDataProvider {
    override fun findUser(uid: Long, chatId: Long): UserData? {
        return UserData(uid, chatId, MAIN_STATE, null)
    }
}
