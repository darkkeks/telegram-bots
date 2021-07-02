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
        register(0x01, ReportEventButton::class) { ReportEventButton(it.popInt()) }
        register(0x02, EditButton::class) { EditButton() }
        register(0x03, ReportButton::class) { ReportButton() }
        register(0x04, CreateClassButton::class) { CreateClassButton() }
        register(0x05, CancelButton::class) { CancelButton() }
        register(0x06, MainMenuButton::class) { MainMenuButton() }
        register(0x07, SkipButton::class) { SkipButton() }
        register(0x08, RemoveButton::class) { RemoveButton() }
        register(0x09, RemoveClassButton::class) { RemoveClassButton(it.popInt()) }
        register(0x0A, NowButton::class) { NowButton() }
        register(0x0B, AddCommentButton::class) { AddCommentButton(it.popInt()) }
        register(0x0C, EditEventButton::class) { EditEventButton(it.popInt()) }
        register(0x0D, YesButton::class) { YesButton() }
        register(0x0E, NoButton::class) { NoButton() }
    }
}

class CancelButton : TextButtonState("❌ Cancel")
class SkipButton : TextButtonState("➡️ Skip")
class MainMenuButton : TextButtonState("◀️ Back")
class EditButton : TextButtonState("✏️ Edit")
class RemoveButton : TextButtonState("🗑 Delete")
class ReportButton : TextButtonState("📌 Report")
class CreateClassButton : TextButtonState("🆕 Create")
class NowButton : TextButtonState("⏳ Now")
class YesButton : TextButtonState("✅ Yes")
class NoButton : TextButtonState("❌ No")

class EditEventButton(val eid: Int) : TextButtonState("✏️ Edit event", { pushInt(eid) })
class AddCommentButton(val eid: Int) : TextButtonState("💠 Add comment", { pushInt(eid) })

class ReportEventButton(val ecid: Int) : ButtonState({ pushInt(ecid) })
class RemoveClassButton(val ecid: Int) : ButtonState({ pushInt(ecid) })
