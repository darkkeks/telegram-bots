package ru.darkkeks.telegram.lifestats.handlers

import org.springframework.stereotype.Component
import ru.darkkeks.telegram.core.api.Telegram
import ru.darkkeks.telegram.core.buildKeyboard
import ru.darkkeks.telegram.core.unwrap
import ru.darkkeks.telegram.lifestats.AddCommentButton
import ru.darkkeks.telegram.lifestats.BackButton
import ru.darkkeks.telegram.lifestats.ButtonConverter
import ru.darkkeks.telegram.lifestats.CancelButton
import ru.darkkeks.telegram.lifestats.Constants.EDIT_EVENT_COMMENT_STATE
import ru.darkkeks.telegram.lifestats.Constants.EDIT_EVENT_STATE
import ru.darkkeks.telegram.lifestats.Context
import ru.darkkeks.telegram.lifestats.EditEventButton
import ru.darkkeks.telegram.lifestats.Event
import ru.darkkeks.telegram.lifestats.EventClassRepository
import ru.darkkeks.telegram.lifestats.EventRepository
import ru.darkkeks.telegram.lifestats.HandlerFactory
import ru.darkkeks.telegram.lifestats.MessageContext
import ru.darkkeks.telegram.lifestats.StateData
import ru.darkkeks.telegram.lifestats.service.UserService
import ru.darkkeks.telegram.lifestats.setState
import ru.darkkeks.telegram.lifestats.util.handlerList
import ru.darkkeks.telegram.lifestats.util.withState
import java.time.Duration

@Component
class EditEventHandlers(
    private val telegram: Telegram,
    private val userService: UserService,
    private val commonMessages: CommonMessages,
    private val buttonConverter: ButtonConverter,
    private val eventRepository: EventRepository,
    private val eventClassRepository: EventClassRepository,
) : HandlerFactory {
    data class EditEventStateData(
        val eid: Int,
    ) : StateData

    override fun handlers() = handlerList {
        withState(EDIT_EVENT_STATE) {
            callback<BackButton>(commonMessages::enterMainMenuState)
            callback<AddCommentButton>(::enterAddCommentState)
        }

        withState(EDIT_EVENT_COMMENT_STATE) {
            callback<BackButton>(commonMessages::enterMainMenuState)
            fallback(::handleComment)
        }
    }

    fun enterEditEventState(context: Context, event: Event) {
        val type = eventClassRepository.findById(event.ecid).unwrap()
        if (type == null) {
            telegram.sendMessage(context.message.chat.id, "Событие не найдено")
            return
        }

        val keyboard = buildKeyboard {
            add(BackButton())
            add(EditEventButton(event.eid))
            add(AddCommentButton(event.eid))
        }

        userService.saveUserData(context.user.setState(EDIT_EVENT_STATE, EditEventStateData(event.eid)))
        telegram.sendMessage(
            context.message.chat.id,
            """
                Событие типа ${type.name}
                
                Начало: ${event.begin}
                Конец: ${event.end}
                Продолжительность: ${Duration.between(event.end, event.begin)}
                Количество: ${event.count}
                Комментарий: ${event.comment}
            """.trimIndent(),
            replyMarkup = buttonConverter.serialize(keyboard),
        )
    }

    private fun enterAddCommentState(context: Context) {
        val state = context.user.stateData
        check(state is EditEventStateData) { "State has to contain event id" }
        enterAddCommentState(context, state.eid)
    }

    fun enterAddCommentState(context: Context, eid: Int) {
        userService.saveUserData(context.user.setState(EDIT_EVENT_COMMENT_STATE, EditEventStateData(eid)))
        sendEnterCommentMessage(context)
    }

    private fun handleComment(context: MessageContext) {
        val text = context.message.text?.trim()
        if (text == null) {
            sendEnterCommentMessage(context)
            return
        }

        val eid = (context.user.stateData as EditEventStateData).eid
        val event = eventRepository.findById(eid).unwrap()
        if (event == null) {
            handleNotFound(context)
            return
        }

        val editedEvent = event.copy(
            comment = text,
        )
        eventRepository.save(editedEvent)
        enterEditEventState(context, editedEvent)
    }

    private fun sendEnterCommentMessage(context: Context) {
        val keyboard = buildKeyboard {
            add(CancelButton())
        }
        telegram.sendMessage(
            context.message.chat.id,
            "Напиши новый комментарий:",
            replyMarkup = buttonConverter.serialize(keyboard),
        )
    }

    private fun handleNotFound(context: Context) {
        telegram.sendMessage(
            context.message.chat.id,
            "Редактируемое событие пропало",
        )
        commonMessages.enterMainMenuState(context)
    }
}
