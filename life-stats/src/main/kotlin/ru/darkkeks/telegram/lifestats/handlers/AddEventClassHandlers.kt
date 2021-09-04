package ru.darkkeks.telegram.lifestats.handlers

import org.springframework.stereotype.Component
import ru.darkkeks.telegram.core.ButtonConverter
import ru.darkkeks.telegram.core.Context
import ru.darkkeks.telegram.core.HandlerFactory
import ru.darkkeks.telegram.core.StateData
import ru.darkkeks.telegram.core.api.Telegram
import ru.darkkeks.telegram.core.buildKeyboard
import ru.darkkeks.telegram.core.handlerList
import ru.darkkeks.telegram.core.setState
import ru.darkkeks.telegram.core.withState
import ru.darkkeks.telegram.lifestats.CancelButton
import ru.darkkeks.telegram.lifestats.Constants
import ru.darkkeks.telegram.lifestats.EventClass
import ru.darkkeks.telegram.lifestats.EventClassRepository
import ru.darkkeks.telegram.lifestats.EventType
import ru.darkkeks.telegram.lifestats.SkipButton
import ru.darkkeks.telegram.lifestats.service.UserService

@Component
class AddEventClassHandlers(
    private val telegram: Telegram,
    private val userService: UserService,
    private val eventClassRepository: EventClassRepository,
    private val buttonConverter: ButtonConverter,
    private val commonMessages: CommonMessages,
) : HandlerFactory {
    data class AddEventClassStateData(
        val name: String?,
        val description: String?,
    ) : StateData

    override fun handlers() = handlerList {
        withState(Constants.NAME_STATE) {
            fallback { context ->
                val name = context.message.text?.trim()
                if (name.isNullOrBlank()) {
                    sendCreateNameMessage(context)
                } else {
                    val data = AddEventClassStateData(name, null)
                    userService.saveUserData(context.user.setState(Constants.DESCRIPTION_STATE, data))
                    sendCreateDescriptionMessage(context)
                }
            }
            callback<CancelButton>(commonMessages::enterMainMenuState)
            fallbackCallback(::sendCreateNameMessage)
        }

        withState(Constants.DESCRIPTION_STATE) {
            fallback { context ->
                val description = context.message.text?.trim()
                if (description.isNullOrBlank()) {
                    sendCreateDescriptionMessage(context)
                } else {
                    val data = (context.user.stateData as AddEventClassStateData)
                        .copy(description = description)
                    userService.saveUserData(context.user.setState(Constants.TYPE_STATE, data))
                    sendCreateTypeMessage(context)
                }
            }
            callback<CancelButton>(commonMessages::enterMainMenuState)
            callback<SkipButton> { context ->
                val data = (context.user.stateData as AddEventClassStateData)
                    .copy(description = null)
                userService.saveUserData(context.user.setState(Constants.TYPE_STATE, data))
                sendCreateTypeMessage(context)
            }
            fallbackCallback(::sendCreateDescriptionMessage)
        }

        withState(Constants.TYPE_STATE) {
            fallback { context ->
                val type = context.message.text
                    ?.let { EventType.ofString(it) }

                if (type == null) {
                    sendCreateTypeMessage(context)
                } else {
                    val data = context.user.stateData as AddEventClassStateData

                    val eventClass = EventClass(
                        uid = context.user.uid,
                        textId = "default",
                        name = data.name!!,
                        description = data.description,
                        type = type,
                    )

                    eventClassRepository.save(eventClass)

                    telegram.sendMessage(context.message.chat.id, "Тип успешно создан!")
                    commonMessages.enterMainMenuState(context)
                }
            }
            callback<CancelButton>(commonMessages::enterMainMenuState)
            fallbackCallback(::sendCreateTypeMessage)
        }
    }

    fun enterCreateNameState(context: Context) {
        userService.saveUserData(context.user.setState(Constants.NAME_STATE))
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
