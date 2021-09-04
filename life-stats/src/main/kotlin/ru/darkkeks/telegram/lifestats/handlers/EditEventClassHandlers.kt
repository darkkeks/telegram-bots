package ru.darkkeks.telegram.lifestats.handlers

import org.springframework.stereotype.Component
import ru.darkkeks.telegram.core.ButtonConverter
import ru.darkkeks.telegram.core.Context
import ru.darkkeks.telegram.core.HandlerFactory
import ru.darkkeks.telegram.core.StateData
import ru.darkkeks.telegram.core.api.ParseMode
import ru.darkkeks.telegram.core.api.Telegram
import ru.darkkeks.telegram.core.buildKeyboard
import ru.darkkeks.telegram.core.handlerList
import ru.darkkeks.telegram.core.setState
import ru.darkkeks.telegram.core.unwrap
import ru.darkkeks.telegram.lifestats.BackButton
import ru.darkkeks.telegram.lifestats.ChangeDescriptionButton
import ru.darkkeks.telegram.lifestats.ChangeNameButton
import ru.darkkeks.telegram.lifestats.ChangeTypeButton
import ru.darkkeks.telegram.lifestats.Constants.EDIT_CLASS_STATE
import ru.darkkeks.telegram.lifestats.EventClassRepository
import ru.darkkeks.telegram.lifestats.EventRepository
import ru.darkkeks.telegram.lifestats.service.UserService

@Component
class EditEventClassHandlers(
    private val eventClassRepository: EventClassRepository,
    private val eventRepository: EventRepository,
    private val telegram: Telegram,
    private val buttonConverter: ButtonConverter,
    private val commonMessages: CommonMessages,
    private val userService: UserService,
) : HandlerFactory {
    data class EditEventClassStateData(
        val ecid: Int,
    ) : StateData

    override fun handlers() = handlerList(EDIT_CLASS_STATE) {
        callback<ChangeNameButton>(::changeNameHandler)
        callback<ChangeDescriptionButton>(::changeDescriptionHandler)
        callback<ChangeTypeButton>(::changeTypeHandler)

        callback<BackButton>(commonMessages::enterMainMenuState)

        fallback(::fallback)
        fallbackCallback(::fallback)
    }

    private fun fallback(context: Context) {
        telegram.sendMessage(context.message.chat.id, "Кнопку нажми Э")
    }

    fun enterEditEventClassState(context: Context, ecid: Int) {
        val type = eventClassRepository.findById(ecid).unwrap()
        if (type == null) {
            telegram.sendMessage(context.message.chat.id, "Тип не найден")
            return
        }

        val eventCount = eventRepository.countByEcid(ecid)

        val keyboard = buildKeyboard {
            add(ChangeNameButton())
            add(ChangeDescriptionButton())
            add(ChangeTypeButton())
        }

        userService.saveUserData(context.user.setState(EDIT_CLASS_STATE, EditEventClassStateData(ecid)))
        telegram.sendMessage(
            context.message.chat.id, """
                ${type.name} (${type.type.name.lowercase()})
                
                ${type.description}
                
                Всего событий: `${eventCount}`
            """.trimIndent(),
            replyMarkup = buttonConverter.serialize(keyboard),
            parseMode = ParseMode.MARKDOWN_V2,
        )
    }

    private fun changeNameHandler(context: Context) {
    }

    private fun changeDescriptionHandler(context: Context) {
    }

    private fun changeTypeHandler(context: Context) {
    }
}
