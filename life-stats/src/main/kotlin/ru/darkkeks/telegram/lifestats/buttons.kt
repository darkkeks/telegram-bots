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
        register(0x01, ReportClassButton::class) { ReportClassButton(it.popInt()) }
        register(0x02, EventClassesButton::class) { EventClassesButton() }
        register(0x03, ReportButton::class) { ReportButton() }
        register(0x04, CreateClassButton::class) { CreateClassButton() }
        register(0x05, CancelButton::class) { CancelButton() }
        register(0x06, BackButton::class) { BackButton() }
        register(0x07, SkipButton::class) { SkipButton() }
        register(0x08, RemoveButton::class) { RemoveButton() }
        register(0x09, RemoveClassButton::class) { RemoveClassButton(it.popInt()) }
        register(0x0A, NowButton::class) { NowButton() }
        register(0x0B, AddCommentButton::class) { AddCommentButton(it.popInt()) }
        register(0x0C, EditEventButton::class) { EditEventButton(it.popInt()) }
        register(0x0D, YesButton::class) { YesButton() }
        register(0x0E, NoButton::class) { NoButton() }
        register(0x0F, EditButton::class) { EditButton() }
        register(0x10, ChangeNameButton::class) { ChangeNameButton() }
        register(0x11, ChangeDescriptionButton::class) { ChangeDescriptionButton() }
        register(0x12, ChangeTypeButton::class) { ChangeTypeButton() }
        register(0x13, EditClassButton::class) { EditClassButton(it.popInt()) }
    }
}

class CancelButton : TextButtonState("❌ Cancel")
class SkipButton : TextButtonState("➡️ Skip")
class BackButton : TextButtonState("◀️ Back")
class EventClassesButton : TextButtonState("🗒️ Event types")
class RemoveButton : TextButtonState("🗑 Delete")
class ReportButton : TextButtonState("📌 Report")
class CreateClassButton : TextButtonState("🆕 Create")
class NowButton : TextButtonState("⏳ Now")
class YesButton : TextButtonState("✅ Yes")
class NoButton : TextButtonState("❌ No")
class EditButton : TextButtonState("✏️ Edit")
class ChangeNameButton : TextButtonState("Name")
class ChangeDescriptionButton : TextButtonState("Description")
class ChangeTypeButton : TextButtonState("Type")

class EditEventButton(val eid: Int) : TextButtonState("✏️ Edit event", { pushInt(eid) })
class AddCommentButton(val eid: Int) : TextButtonState("💠 Add comment", { pushInt(eid) })

class ReportClassButton(val ecid: Int) : ButtonState({ pushInt(ecid) })
class EditClassButton(val ecid: Int) : ButtonState({ pushInt(ecid) })
class RemoveClassButton(val ecid: Int) : ButtonState({ pushInt(ecid) })
