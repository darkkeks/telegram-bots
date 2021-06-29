package ru.darkkeks.telegram.lifestats.handlers

import org.springframework.stereotype.Component
import ru.darkkeks.telegram.core.api.ParseMode
import ru.darkkeks.telegram.core.api.Telegram
import ru.darkkeks.telegram.core.buildKeyboard
import ru.darkkeks.telegram.lifestats.ButtonConverter
import ru.darkkeks.telegram.lifestats.CallbackButtonContext
import ru.darkkeks.telegram.lifestats.CancelButton
import ru.darkkeks.telegram.lifestats.Constants.MAX_CLASSES
import ru.darkkeks.telegram.lifestats.Context
import ru.darkkeks.telegram.lifestats.CreateClassButton
import ru.darkkeks.telegram.lifestats.EditButton
import ru.darkkeks.telegram.lifestats.EventClass
import ru.darkkeks.telegram.lifestats.EventClassButton
import ru.darkkeks.telegram.lifestats.EventClassRepository
import ru.darkkeks.telegram.lifestats.EventType
import ru.darkkeks.telegram.lifestats.HandlerFactory
import ru.darkkeks.telegram.lifestats.MainMenuButton
import ru.darkkeks.telegram.lifestats.RemoveButton
import ru.darkkeks.telegram.lifestats.RemoveClassButton
import ru.darkkeks.telegram.lifestats.ReportButton
import ru.darkkeks.telegram.lifestats.SkipButton
import ru.darkkeks.telegram.lifestats.resetState
import ru.darkkeks.telegram.lifestats.service.UserService
import ru.darkkeks.telegram.lifestats.service.UserService.Companion.MAIN_STATE
import ru.darkkeks.telegram.lifestats.setState
import ru.darkkeks.telegram.lifestats.updateState
import ru.darkkeks.telegram.lifestats.util.handlerList
import ru.darkkeks.telegram.lifestats.util.withState

@Component
class MainHandlers(
    private val telegram: Telegram,
    private val eventClassRepository: EventClassRepository,
    private val buttonConverter: ButtonConverter,
    private val addEventClassHandlers: AddEventClassHandlers,
    private val messages: Messages,
) : HandlerFactory {
    override fun handlers() = handlerList {
        withState(MAIN_STATE) {
            command("help", messages::sendMainMenuMessage)
            command("start", messages::sendMainMenuMessage)

            command("report", ::reportHandler)
            callback<ReportButton>(::reportHandler)
            callback(::eventClassHandler)

            command("edit", ::editHandler)
            callback<EditButton>(::editHandler)
            callback<CreateClassButton>(addEventClassHandlers::enterCreateNameState)
            callback<RemoveButton>(::removeHandler)
            callback(::removeClassHandler)

            callback<MainMenuButton>(messages::sendMainMenuMessage)
            fallback(messages::sendMainMenuMessage)
        }
    }

    private fun editText(types: List<EventClass>): String {
        if (types.isEmpty()) {
            return "У тебя пока не создано ни одного типа событий!"
        }
        var message = "Типы событий:\n"
        types.forEachIndexed { index, type ->
            message += "${index + 1}. ${type.name}\n"
        }
        return message
    }

    private fun editHandler(context: Context) {
        val types = eventClassRepository.findAllByUid(context.user.id)
        val keyboard = buildKeyboard {
            add(MainMenuButton())
            if (types.size < MAX_CLASSES) {
                add(CreateClassButton())
            }
            if (types.isNotEmpty()) {
                add(RemoveButton())
            }
        }
        telegram.sendMessage(
            context.message.chat.id,
            editText(types),
            replyMarkup = buttonConverter.serialize(keyboard),
        )
    }

    private fun removeHandler(context: Context) {
        val types = eventClassRepository.findAllByUid(context.user.id)
        if (types.isEmpty()) {
            telegram.sendMessage(context.user.chatId, "Удалять нечего :/")
        } else {
            val keyboard = buildKeyboard {
                for (type in types) {
                    add(type.name, RemoveClassButton(type.ecid))
                }
            }
            telegram.sendMessage(
                context.message.chat.id,
                "Выберите тип, который надо удалить:",
                replyMarkup = buttonConverter.serialize(keyboard)
            )
        }
    }

    private fun removeClassHandler(context: CallbackButtonContext<RemoveClassButton>) {
        val typeOptional = eventClassRepository.findById(context.state.ecid)
        if (typeOptional.isEmpty) {
            if (context.answer()) {
                telegram.answerCallbackQuery(context.callbackQuery.id, "Этот тип не найден!")
            }
        } else {
            val type = typeOptional.get()
            eventClassRepository.delete(type)
            telegram.sendMessage(context.message.chat.id, "Тип ${type.name} был удален!")
        }
    }

    private fun reportHandler(context: Context) {
        val types = eventClassRepository.findAllByUid(context.user.id)
        if (types.isEmpty()) {
            telegram.sendMessage(context.user.chatId, "Нету ни одного типа события :(")
        } else {
            val keyboard = buildKeyboard {
                for (eventClass in types) {
                    add(eventClass.name, EventClassButton(eventClass.ecid))
                }
            }
            telegram.sendMessage(
                context.user.chatId, "Вот кнопки",
                replyMarkup = buttonConverter.serialize(keyboard)
            )
        }
    }

    private fun eventClassHandler(context: CallbackButtonContext<EventClassButton>) {
        val eventClass = eventClassRepository.findById(context.state.ecid)
        if (eventClass.isPresent) {
            telegram.sendMessage(
                context.user.chatId,
                "Ты тыкнул на кнопку `${eventClass.get().name}`",
                parseMode = ParseMode.MARKDOWN_V2
            )
        } else {
            telegram.sendMessage(context.user.chatId, "Ты тыкнул на кнопку с несуществующим классом 🤯")
        }
    }
}

@Component
class AddEventClassHandlers(
    private val telegram: Telegram,
    private val userService: UserService,
    private val eventClassRepository: EventClassRepository,
    private val buttonConverter: ButtonConverter,
    private val messages: Messages,
) : HandlerFactory {

    companion object {
        const val NAME_STATE = "create__enter_name"
        const val DESCRIPTION_STATE = "create__enter_description"
        const val TYPE_STATE = "create__enter_type"

        const val NAME_KEY = "create_name"
        const val DESCRIPTION_KEY = "create_description"
    }

    override fun handlers() = handlerList {
        withState(NAME_STATE) {
            fallback { context ->
                val name = context.message.text?.trim()
                if (name.isNullOrBlank()) {
                    sendCreateNameMessage(context)
                } else {
                    userService.saveUserData(
                        context.user.setState(
                            DESCRIPTION_STATE,
                            mapOf(NAME_KEY to name)
                        )
                    )
                    sendCreateDescriptionMessage(context)
                }
            }
            callback<CancelButton> { context ->
                userService.saveUserData(context.user.resetState())
                messages.sendMainMenuMessage(context)
            }
            fallbackCallback(::sendCreateNameMessage)
        }

        withState(DESCRIPTION_STATE) {
            fallback { context ->
                val description = context.message.text?.trim()
                if (description.isNullOrBlank()) {
                    sendCreateDescriptionMessage(context)
                } else {
                    userService.saveUserData(
                        context.user.updateState(
                            TYPE_STATE,
                            mapOf(DESCRIPTION_KEY to description)
                        )
                    )
                    sendCreateTypeMessage(context)
                }
            }
            callback<CancelButton> { context ->
                userService.saveUserData(context.user.resetState())
                messages.sendMainMenuMessage(context)
            }
            callback<SkipButton> { context ->
                userService.saveUserData(
                    context.user.updateState(
                        TYPE_STATE,
                        mapOf(DESCRIPTION_KEY to null)
                    )
                )
                sendCreateTypeMessage(context)
            }
            fallbackCallback(::sendCreateDescriptionMessage)
        }

        withState(TYPE_STATE) {
            fallback { context ->
                val type = context.message.text
                    ?.let { EventType.ofString(it) }

                if (type == null) {
                    sendCreateTypeMessage(context)
                } else {
                    val name = context.user.stateData[NAME_KEY] as String
                    val description = context.user.stateData[DESCRIPTION_KEY] as String?

                    val eventClass = EventClass(
                        uid = context.user.id,
                        textId = "default",
                        name = name,
                        description = description,
                        type = type,
                    )

                    eventClassRepository.save(eventClass)
                    userService.saveUserData(context.user.resetState())

                    telegram.sendMessage(context.message.chat.id, "Тип успешно создан!")
                    messages.sendMainMenuMessage(context)
                }
            }
            callback<CancelButton> { context ->
                userService.saveUserData(context.user.resetState())
                messages.sendMainMenuMessage(context)
            }
            fallbackCallback(::sendCreateTypeMessage)
        }
    }

    fun enterCreateNameState(context: Context) {
        userService.saveUserData(context.user.setState(NAME_STATE))
        sendCreateNameMessage(context)
    }

    fun sendCreateNameMessage(context: Context) {
        val keyboard = buildKeyboard {
            add(CancelButton())
        }
        telegram.sendMessage(
            context.message.chat.id,
            "Напиши название для типа событий",
            replyMarkup = buttonConverter.serialize(keyboard)
        )
    }

    fun sendCreateDescriptionMessage(context: Context) {
        val keyboard = buildKeyboard {
            add(CancelButton())
            add(SkipButton())
        }
        telegram.sendMessage(
            context.message.chat.id,
            "Напиши описание для типа событий",
            replyMarkup = buttonConverter.serialize(keyboard)
        )
    }

    fun sendCreateTypeMessage(context: Context) {
        val keyboard = buildKeyboard {
            add(CancelButton())
        }
        val types = EventType.values().joinToString(", ")
        telegram.sendMessage(
            context.message.chat.id,
            "Напиши какого рода события хочешь трекать (одно из $types)",
            replyMarkup = buttonConverter.serialize(keyboard)
        )
    }
}

@Component
class Messages(
    private val telegram: Telegram,
    private val buttonConverter: ButtonConverter,
) {
    fun sendMainMenuMessage(context: Context) {
        val keyboard = buildKeyboard {
            add(ReportButton())
            add(EditButton())
        }
        telegram.sendMessage(
            context.user.chatId,
            """
                Здесь должен быть main текст.
            """.trimIndent(),
            replyMarkup = buttonConverter.serialize(keyboard),
        )
    }
}
