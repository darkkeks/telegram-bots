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
import ru.darkkeks.telegram.lifestats.Constants.REMOVE_CLASS_STATE
import ru.darkkeks.telegram.lifestats.EventClassRepository
import ru.darkkeks.telegram.lifestats.EventRepository
import ru.darkkeks.telegram.lifestats.NoButton
import ru.darkkeks.telegram.lifestats.YesButton
import ru.darkkeks.telegram.lifestats.service.UserService

@Component
class RemoveEventClassHandlers(
    private val eventClassRepository: EventClassRepository,
    private val eventRepository: EventRepository,
    private val userService: UserService,
    private val telegram: Telegram,
    private val commonMessages: CommonMessages,
    private val buttonConverter: ButtonConverter,
) : HandlerFactory {
    data class RemoveEventClassStateData(val ecid: Int) : StateData

    override fun handlers() = handlerList(REMOVE_CLASS_STATE) {
        callback<YesButton>(::remove)
        callback<NoButton>(::cancel)
        command("/cancel", ::cancel)

        fallback(::fallback)
        fallbackCallback(::fallback)
    }

    fun enterRemoveClassState(context: Context, ecid: Int) {
        val keyboard = buildKeyboard {
            add(NoButton())
            add(YesButton())
        }

        userService.saveUserData(context.user.setState(REMOVE_CLASS_STATE, RemoveEventClassStateData(ecid)))

        telegram.sendMessage(
            context.message.chat.id,
            "Вы уверены что хотите удалить тип событий? Будут удалены все события этого типа",
            replyMarkup = buttonConverter.serialize(keyboard),
        )
    }

    private fun remove(context: Context) {
        val ecid = (context.user.stateData as RemoveEventClassStateData).ecid
        val type = eventClassRepository.findById(ecid).unwrap()
        if (type == null) {
            telegram.sendMessage(
                context.message.chat.id,
                "Данного типа событий уже не существует!",
            )
            commonMessages.enterMainMenuState(context)
        } else {
            eventRepository.deleteAllByEcid(ecid)
            eventClassRepository.delete(type)

            telegram.sendMessage(
                context.message.chat.id,
                "Тип событий `${type.name}` был удален ",
                parseMode = ParseMode.MARKDOWN_V2,
            )

            commonMessages.enterMainMenuState(context)
        }
    }

    private fun cancel(context: Context) {
        telegram.sendMessage(
            context.message.chat.id,
            "Удаление отменено!",
        )
        commonMessages.enterMainMenuState(context)
    }

    private fun fallback(context: Context) {
        telegram.sendMessage(
            context.message.chat.id,
            "Нажми на одну из кнопок чтобы подтвердить. Либо /cancel для отмены удаления.",
        )
    }
}
