package ru.darkkeks.telegram.hseremind

import org.springframework.stereotype.Component

@Component
class UserConfigService(
        val userRepository: UserRepository,
        val sourceFetchers: List<SourceFetchService>
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
        val previousSpec = config.put(user.id, user.spec)
        if (previousSpec != null) {
            removeSpec(previousSpec)
        }
        addSpec(user.spec)
    }

    fun addSpec(spec: UserSpec) {
        forEachRule(spec) { fetcher, source -> fetcher.addSource(source) }
    }

    fun removeSpec(spec: UserSpec) {
        forEachRule(spec) { fetcher, source -> fetcher.removeSource(source) }
    }

    fun forEachRule(spec: UserSpec, block: (SourceFetchService, Source) -> Unit) {
        sourceFetchers.forEach { fetcher ->
            spec.chats.forEach { chat ->
                (chat.lectureRules ?: chat.rules)?.forEach { rule ->
                    block(fetcher, rule.source)
                }
                chat.youtubeRules?.forEach { rule ->
                    block(fetcher, rule.source)
                }
            }
        }
    }

}
