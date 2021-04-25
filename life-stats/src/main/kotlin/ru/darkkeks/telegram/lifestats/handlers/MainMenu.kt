package ru.darkkeks.telegram.lifestats.handlers

import org.springframework.stereotype.Component
import ru.darkkeks.telegram.core.api.ParseMode
import ru.darkkeks.telegram.core.api.Telegram
import ru.darkkeks.telegram.core.buildKeyboard
import ru.darkkeks.telegram.core.handle_wip.ButtonState
import ru.darkkeks.telegram.core.serialize.pushInt
import ru.darkkeks.telegram.lifestats.ButtonConverter
import ru.darkkeks.telegram.lifestats.Context
import ru.darkkeks.telegram.lifestats.HandlerFactory
import ru.darkkeks.telegram.lifestats.handlerList
import ru.darkkeks.telegram.lifestats.repository.EventClassRepository

@Component
class MainMenu(
    val telegram: Telegram,
    val buttonConverter: ButtonConverter,
    val eventClassRepository: EventClassRepository,
) : HandlerFactory {
    override fun handlers() = handlerList {
        command("help", ::commonHandler)
        command("start", ::commonHandler)

        command("edit", ::editHandler)
        callback<EditButton>(::editHandler)

        command("report", ::reportHandler)
        callback<ReportButton>(::reportHandler)

        callback<EventClassButton> {
            val eventClass = eventClassRepository.findById(it.state.ecid)
            if (eventClass.isPresent) {
                telegram.sendMessage(it.userData.chatId, "Ты тыкнул на кнопку `${eventClass.get().name}`",
                    parseMode = ParseMode.MARKDOWN_V2)
            } else {
                telegram.sendMessage(it.userData.chatId, "Ты тыкнул на кнопку с несуществующим классом 🤯")
            }
        }

        fallback {
            telegram.sendMessage(it.userData.chatId, "Тут дефолт")
        }
    }

    /**
     * Клавиатура с главным меню
     */
    private fun commonKeyboard() = buildKeyboard {
        add("📌 Report", ReportButton())
        add("📝 Edit", EditButton())
    }

    private fun commonHandler(context: Context) {
        telegram.sendMessage(context.userData.chatId, "Тут /help",
            replyMarkup = buttonConverter.serialize(commonKeyboard()))
    }

    private fun editHandler(context: Context) {
        telegram.sendMessage(context.userData.chatId, "Здесь должна быть возможность редактировать штуки")
    }

    private fun reportHandler(context: Context) {
        val keyboard = buildKeyboard {
            for (eventClass in eventClassRepository.findAll()) {
                add(eventClass.name, EventClassButton(eventClass.ecid))
            }
        }

        telegram.sendMessage(context.userData.chatId, "Вот кнопки",
            replyMarkup = buttonConverter.serialize(keyboard))
    }
}

class EditButton : ButtonState()
class ReportButton : ButtonState()

class EventClassButton(val ecid: Int) : ButtonState({ pushInt(ecid) })
