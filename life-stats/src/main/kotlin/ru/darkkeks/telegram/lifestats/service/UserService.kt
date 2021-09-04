package ru.darkkeks.telegram.lifestats.service

import org.springframework.stereotype.Component
import ru.darkkeks.telegram.core.MAIN_STATE
import ru.darkkeks.telegram.core.UserData
import ru.darkkeks.telegram.core.UserDataProvider
import ru.darkkeks.telegram.core.fromJson
import ru.darkkeks.telegram.core.toJson
import ru.darkkeks.telegram.lifestats.User
import ru.darkkeks.telegram.lifestats.UserRepository

@Component
class UserService(
    private val userRepository: UserRepository,
) : UserDataProvider {
    fun getOrCreate(uid: Long): User {
        return userRepository.findById(uid).orElseGet {
            User(uid, MAIN_STATE, toJson(null)).also { user ->
                userRepository.insert(user)
            }
        }
    }

    fun saveUserData(user: UserData) {
        userRepository.save(User(
            user.uid,
            user.state,
            toJson(user.stateData),
        ))
    }

    override fun findUser(uid: Long, chatId: Long): UserData? {
        if (uid != chatId) {
            return null
        }
        val user = getOrCreate(uid)
        return UserData(
            user.uid,
            chatId,
            user.state,
            fromJson(user.stateData),
        )
    }
}
