package ru.darkkeks.telegram.lifestats.handlers

import org.springframework.stereotype.Component
import ru.darkkeks.telegram.core.api.Telegram
import ru.darkkeks.telegram.core.buildKeyboard
import ru.darkkeks.telegram.core.unwrap
import ru.darkkeks.telegram.lifestats.AddCommentButton
import ru.darkkeks.telegram.lifestats.ButtonConverter
import ru.darkkeks.telegram.lifestats.CallbackButtonContext
import ru.darkkeks.telegram.lifestats.Constants.MAIN_STATE
import ru.darkkeks.telegram.lifestats.Constants.MAX_CLASSES
import ru.darkkeks.telegram.lifestats.Context
import ru.darkkeks.telegram.lifestats.CreateClassButton
import ru.darkkeks.telegram.lifestats.EditButton
import ru.darkkeks.telegram.lifestats.EditClassButton
import ru.darkkeks.telegram.lifestats.EventClassesButton
import ru.darkkeks.telegram.lifestats.EventClass
import ru.darkkeks.telegram.lifestats.ReportClassButton
import ru.darkkeks.telegram.lifestats.EventClassRepository
import ru.darkkeks.telegram.lifestats.HandlerFactory
import ru.darkkeks.telegram.lifestats.BackButton
import ru.darkkeks.telegram.lifestats.EditEventButton
import ru.darkkeks.telegram.lifestats.RemoveButton
import ru.darkkeks.telegram.lifestats.RemoveClassButton
import ru.darkkeks.telegram.lifestats.ReportButton
import ru.darkkeks.telegram.lifestats.util.handlerList

@Component
class MainHandlers(
    private val telegram: Telegram,
    private val eventClassRepository: EventClassRepository,
    private val buttonConverter: ButtonConverter,
    private val addEventClassHandlers: AddEventClassHandlers,
    private val reportEventClassHandlers: ReportEventClassHandlers,
    private val editEventClassHandlers: EditEventClassHandlers,
    private val removeEventClassHandlers: RemoveEventClassHandlers,
    private val editEventHandlers: EditEventHandlers,
    private val commonMessages: CommonMessages,
) : HandlerFactory {

    override fun handlers() = handlerList(MAIN_STATE) {
        command("help", commonMessages::sendMainMenuMessage)
        command("start", commonMessages::sendMainMenuMessage)
        callback<BackButton>(commonMessages::sendMainMenuMessage)

        command("report", ::reportHandler)
        callback<ReportButton>(::reportHandler)
        callback(::eventClassHandler)

        command("classes", ::eventClassesHandler)
        callback<EventClassesButton>(::eventClassesHandler)

        callback<CreateClassButton>(addEventClassHandlers::enterCreateNameState)

        callback<EditButton>(::editHandler)
        callback<EditClassButton> { context ->
            editEventClassHandlers.enterEditEventClassState(context, context.state.ecid)
        }

        callback<RemoveButton>(::removeHandler)
        callback<RemoveClassButton> { context ->
            removeEventClassHandlers.enterRemoveClassState(context, context.state.ecid)
        }

        callback<EditEventButton> { context ->
            editEventHandlers.enterAddCommentState(context, context.state.eid)
        }
        callback<AddCommentButton> { context ->
            editEventHandlers.enterAddCommentState(context, context.state.eid)
        }

        fallback(commonMessages::sendMainMenuMessage)
        fallbackCallback(commonMessages::sendMainMenuMessage)
    }

    private fun editText(types: List<EventClass>): String {
        if (types.isEmpty()) {
            return "У тебя пока не создано ни одного типа событий!"
        }
        var message = "Типы событий:\n"
        types.forEachIndexed { index, type ->
            message += "${index + 1}. ${type.name} (${type.type.name.lowercase()})\n"
        }
        return message
    }

    private fun eventClassesHandler(context: Context) {
        val types = eventClassRepository.findAllByUid(context.user.uid)
        val keyboard = buildKeyboard {
            add(BackButton())
            if (types.size < MAX_CLASSES) {
                add(CreateClassButton())
            }
            if (types.isNotEmpty()) {
                add(EditButton())
                add(RemoveButton())
            }
        }
        telegram.sendMessage(
            context.message.chat.id,
            editText(types),
            replyMarkup = buttonConverter.serialize(keyboard),
        )
    }

    private fun editHandler(context: Context) {
        val types = eventClassRepository.findAllByUid(context.user.uid)
        if (types.isEmpty()) {
            telegram.sendMessage(context.message.chat.id, "Редактировать нечего :/")
        } else {
            val keyboard = buildKeyboard {
                for (type in types) {
                    add(type.name, EditClassButton(type.ecid))
                }
            }
            telegram.sendMessage(
                context.message.chat.id,
                "Выбери тип событий для редактирования",
                replyMarkup = buttonConverter.serialize(keyboard),
            )
        }
    }


    private fun removeHandler(context: Context) {
        val types = eventClassRepository.findAllByUid(context.user.uid)
        if (types.isEmpty()) {
            telegram.sendMessage(context.message.chat.id, "Удалять нечего :/")
        } else {
            val keyboard = buildKeyboard {
                for (type in types) {
                    add(type.name, RemoveClassButton(type.ecid))
                }
            }
            telegram.sendMessage(
                context.message.chat.id,
                "Выберите тип, который надо удалить:",
                replyMarkup = buttonConverter.serialize(keyboard),
            )
        }
    }

    private fun reportHandler(context: Context) {
        val types = eventClassRepository.findAllByUid(context.user.uid)
        if (types.isEmpty()) {
            telegram.sendMessage(context.user.chatId, "Нету ни одного типа события :(")
        } else {
            val keyboard = buildKeyboard {
                for (eventClass in types) {
                    add(eventClass.name, ReportClassButton(eventClass.ecid))
                }
            }
            telegram.sendMessage(
                context.user.chatId,
                "Вот кнопки",
                replyMarkup = buttonConverter.serialize(keyboard),
            )
        }
    }

    private fun eventClassHandler(context: CallbackButtonContext<ReportClassButton>) {
        val type = eventClassRepository.findById(context.state.ecid).unwrap()
        if (type != null) {
            reportEventClassHandlers.enterReportClassState(context, type)
        } else {
            telegram.sendMessage(context.user.chatId, "Ты тыкнул на кнопку с несуществующим классом 🤯")
        }
    }
}


