package ru.darkkeks.telegram.lifestats.handlers

import org.springframework.stereotype.Component
import ru.darkkeks.telegram.core.api.Telegram
import ru.darkkeks.telegram.lifestats.ButtonConverter
import ru.darkkeks.telegram.lifestats.Constants.EDIT_CLASS_STATE
import ru.darkkeks.telegram.lifestats.EventClassRepository
import ru.darkkeks.telegram.lifestats.HandlerFactory
import ru.darkkeks.telegram.lifestats.util.handlerList

@Component
class EditEventClassHandlers(
    private val eventClassRepository: EventClassRepository,
    private val telegram: Telegram,
    private val buttonConverter: ButtonConverter,
    private val commonMessages: CommonMessages,
) : HandlerFactory {

    override fun handlers() = handlerList(EDIT_CLASS_STATE) {



    }

}
