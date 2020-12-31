package ru.darkkeks.telegram.hseremind

import org.springframework.stereotype.Component
import ru.darkkeks.telegram.core.createLogger

@Component
class UserConfigService(
    val userRepository: UserRepository,
    val sourceFetchers: List<SourceFetchService>
) {
    private val logger = createLogger<UserConfigService>()

    private val config: MutableMap<Long, UserSpec> = mutableMapOf()

    fun load() {
        require(config.isEmpty()) { "Config has to be empty to perform initial load" }
        logger.info("Loading initial user configurations")
        userRepository.findAll().forEach { user ->
            val spec = safeParseSpec(user.spec)
            if (spec != null) {
                config[user.id] = spec
                addSpec(user.id, spec)
            }
        }
    }

    fun updateUserConfig(userId: Long, spec: UserSpec) {
        logger.info("Updating spec for user {}", userId)
        val specString = serializeSpec(spec)

        val user = userRepository.findById(userId)
            .map { it.copy(spec = specString) }
            .orElseGet { User(userId, specString) }

        userRepository.save(user)

        val previousSpec = config.put(user.id, spec)
        if (previousSpec != null) {
            removeSpec(user.id, previousSpec)
        }

        addSpec(user.id, spec)
    }

    private fun addSpec(userId: Long, spec: UserSpec) {
        logger.info("Adding spec for user {}", userId)
        forEachRule(spec) { fetcher, source -> fetcher.addSource(source) }
    }

    private fun removeSpec(userId: Long, spec: UserSpec) {
        logger.info("Removing spec for user {}", userId)
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
