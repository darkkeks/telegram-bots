package ru.darkkeks.telegram.lifestats

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.darkkeks.telegram.core.handle_wip.ButtonState
import ru.darkkeks.telegram.core.handle_wip.TextButtonState
import ru.darkkeks.telegram.core.serialize.Registry
import ru.darkkeks.telegram.core.serialize.popInt
import ru.darkkeks.telegram.core.serialize.pushInt

@Configuration
class ButtonConfiguration {
    @Bean
    fun buttonStateRegistry() = Registry<ButtonState>().apply {
        register(0x01, EventClassButton::class) { EventClassButton(it.popInt()) }
        register(0x02, EditButton::class) { EditButton() }
        register(0x03, ReportButton::class) { ReportButton() }
        register(0x04, CreateClassButton::class) { CreateClassButton() }
        register(0x05, CancelButton::class) { CancelButton() }
        register(0x06, MainMenuButton::class) { MainMenuButton() }
        register(0x07, SkipButton::class) { SkipButton() }
        register(0x08, RemoveButton::class) { RemoveButton() }
        register(0x09, RemoveClassButton::class) { RemoveClassButton(it.popInt()) }
    }
}

class CancelButton : TextButtonState("❌ Cancel")
class SkipButton : TextButtonState("➡️ Skip")
class MainMenuButton : TextButtonState("◀️ Back")
class EditButton : TextButtonState("✏️ Edit")
class RemoveButton : TextButtonState("🗑 Delete")
class ReportButton : TextButtonState("📌 Report")
class CreateClassButton : TextButtonState("🆕 Create")
class EventClassButton(val ecid: Int) : ButtonState({ pushInt(ecid) })
class RemoveClassButton(val ecid: Int): ButtonState({ pushInt(ecid) })
