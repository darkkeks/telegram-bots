package ru.darkkeks.telegram.hseremind

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.darkkeks.telegram.core.handle_wip.ButtonState
import ru.darkkeks.telegram.core.serialize.Registry

@Configuration
class ButtonConfiguration {
    @Bean
    fun buttonStateRegistry() = Registry<ButtonState>().apply {
    }
}
