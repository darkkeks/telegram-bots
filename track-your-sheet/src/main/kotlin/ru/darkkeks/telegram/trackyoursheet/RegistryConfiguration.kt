package ru.darkkeks.telegram.trackyoursheet

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.darkkeks.telegram.core.handle.ButtonState
import ru.darkkeks.telegram.core.handle.MessageState
import ru.darkkeks.telegram.core.serialize.Registry

@Configuration
class RegistryConfiguration {
    @Bean
    fun buttonStateRegistry(): Registry<ButtonState> {
        val registry = Registry<ButtonState>()
//        registry.register(0x00, )
        return registry
    }

    @Bean
    fun messageStateRegistry(): Registry<MessageState> {
        val registry = Registry<MessageState>()

        return registry
    }
}
