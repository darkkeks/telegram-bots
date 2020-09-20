package ru.darkkeks.telegram.hseremind

import org.springframework.stereotype.Component

@Component
class UserConfigService(
        val userRepository: UserRepository,
        val sourceFetchService: SourceFetchService
) {

    private val config: MutableMap<Long, UserSpec> = mutableMapOf()

    fun load() {
        require(config.isEmpty()) { "Config has to be empty to perform initial load" }
        userRepository.findAll().forEach { user ->
            config[user.id] = user.spec
            addSpec(user.spec)
        }
    }

    fun update(user: User) {
        val prevSpec = config[user.id]
        if (prevSpec != null) {
            removeSpec(prevSpec)
        }
        config[user.id] = user.spec
        addSpec(user.spec)
    }

    fun addSpec(spec: UserSpec) {
        spec.chats.forEach { chat ->
            chat.rules.forEach { rule ->
                sourceFetchService.addSource(rule.source)
            }
        }
    }

    fun removeSpec(spec: UserSpec) {
        spec.chats.forEach { chat ->
            chat.rules.forEach { rule ->
                sourceFetchService.removeSource(rule.source)
            }
        }
    }

}
