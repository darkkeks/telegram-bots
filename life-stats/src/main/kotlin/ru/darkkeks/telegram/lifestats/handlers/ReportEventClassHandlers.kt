package ru.darkkeks.telegram.lifestats.handlers

import org.springframework.stereotype.Component
import ru.darkkeks.telegram.core.api.ParseMode
import ru.darkkeks.telegram.core.api.Telegram
import ru.darkkeks.telegram.core.buildKeyboard
import ru.darkkeks.telegram.core.unwrap
import ru.darkkeks.telegram.lifestats.AddCommentButton
import ru.darkkeks.telegram.lifestats.ButtonConverter
import ru.darkkeks.telegram.lifestats.Constants
import ru.darkkeks.telegram.lifestats.Context
import ru.darkkeks.telegram.lifestats.EditEventButton
import ru.darkkeks.telegram.lifestats.Event
import ru.darkkeks.telegram.lifestats.EventClass
import ru.darkkeks.telegram.lifestats.EventClassRepository
import ru.darkkeks.telegram.lifestats.EventRepository
import ru.darkkeks.telegram.lifestats.EventType
import ru.darkkeks.telegram.lifestats.HandlerFactory
import ru.darkkeks.telegram.lifestats.MainMenuButton
import ru.darkkeks.telegram.lifestats.NowButton
import ru.darkkeks.telegram.lifestats.StateData
import ru.darkkeks.telegram.lifestats.service.UserService
import ru.darkkeks.telegram.lifestats.setState
import ru.darkkeks.telegram.lifestats.util.handlerList
import java.time.Instant

@Component
class ReportEventClassHandlers(
    private val telegram: Telegram,
    private val userService: UserService,
    private val eventClassRepository: EventClassRepository,
    private val eventRepository: EventRepository,
    private val commonMessages: CommonMessages,
    private val buttonConverter: ButtonConverter,
) : HandlerFactory {
    data class ReportClassState(
        val ecid: Int,
    ) : StateData

    override fun handlers() = handlerList(Constants.REPORT_CLASS_STATE) {
        command("now", ::handleNow)
        callback<NowButton>(::handleNow)

        fallback(::handleNotSupported)
        fallbackCallback(::handleNotSupported)
    }

    private fun handleEvent(context: Context, begin: Instant? = null, end: Instant? = null, count: Long? = null) {
        val ecid = (context.user.stateData as ReportClassState).ecid
        val type = eventClassRepository.findById(ecid).unwrap()
        if (type != null) {
            val event = Event(
                ecid = ecid,
                begin = begin,
                end = end,
                count = count,
                comment = null,
            ).let { eventRepository.save(it) }

            val keyboard = buildKeyboard {
                add(MainMenuButton())
                add(EditEventButton(event.eid))
                add(AddCommentButton(event.eid))
            }
            telegram.sendMessage(
                context.message.chat.id,
                "Сохранили событие: $event",
                replyMarkup = buttonConverter.serialize(keyboard)
            )
        } else {
            handleNotFound(context)
        }
    }

    private fun handleNow(context: Context) {
        val now = Instant.now()
        handleEvent(context, begin = now, end = now)
    }

    private fun handleNotFound(context: Context) {
        telegram.sendMessage(context.message.chat.id, "Редактируемое событие пропало :(")
        commonMessages.enterMainMenuState(context)
    }

    fun enterReportClassState(context: Context, type: EventClass) {
        userService.saveUserData(context.user.setState(Constants.REPORT_CLASS_STATE, ReportClassState(type.ecid)))

        telegram.sendMessage(
            context.user.chatId,
            "Ты тыкнул на кнопку `${type.name}`",
            parseMode = ParseMode.MARKDOWN_V2
        )

        when (type.type) {
            EventType.POINT -> {
                val keyboard = buildKeyboard {
                    add(NowButton())
                }
                telegram.sendMessage(
                    context.message.chat.id,
                    """
                        Нажми "Сейчас", если надо сохранить событие сейчас (с точностью до секунд).
                        Либо введи время в удобном формате
                        - `8:30`
                        - `19:30`
                        - `1:05 PM`
                        - `вчера в 22:08`
                        - `3 часа назад`
                    """.trimIndent(),
                    replyMarkup = buttonConverter.serialize(keyboard),
                    parseMode = ParseMode.MARKDOWN_V2,
                )
            }
            EventType.SEGMENT, EventType.COUNT -> {
                handleNotSupported(context)
            }
        }
    }

    private fun handleNotSupported(context: Context) {
        telegram.sendMessage(context.message.chat.id, "Пока не поддерживается сорян")
        commonMessages.enterMainMenuState(context)
    }
}
