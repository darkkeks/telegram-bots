package ru.darkkeks.telegram.trackyoursheet

import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component
import ru.darkkeks.telegram.core.api.TelegramApi
import ru.darkkeks.telegram.core.buildKeyboard
import ru.darkkeks.telegram.core.handle.*


inline fun <reified M : MessageState, reified B : ButtonState> callbackHandler(
        crossinline block: CallbackContext<TrackUserState, M, B>.() -> Unit
): CallbackHandler = abstractCallbackHandler(block)


enum class TrackGlobalState {
    NONE,
    NEW_RANGE
}

data class TrackUserState(
        val userId: Int,
        val chatId: Long,
        val state: TrackGlobalState = TrackGlobalState.NONE
) : UserState()


class GoBackButton : TextButton("◀ Назад")

class CreateNewRangeButton : TextButton("*️⃣ Создать ренж")
class ListRangesButton : TextButton("📝 Список ренжей")

class MainMenuMessageState : MessageState()
class RangeListMessageState : MessageState()

interface HandlerFactory {
    val priority get() = 0

    fun handlers(): List<Handler>
}

@Component
class MainMenu(
        private val telegramApi: TelegramApi,
        private val userStateService: TrackUserStateService,
        private val statefulMessagesService: StatefulMessagesService,

        private val rangeList: RangeList
) : HandlerFactory {

    fun render() = StatefulMessageRender(
            MainMenuMessageState(),
            """
                🔥 Привет, я умею наблюдать за гугл табличками. 🔥
                
                📌 _Ренжем_ я называю диапазон на некотором листе в гугл таблице.
                
                ✨ С любыми вопросами по работе бота можно писать @darkkeks.
            """.trimIndent(),
            buildKeyboard {
                add(CreateNewRangeButton())
                add(ListRangesButton())
            }
    )

    override fun handlers(): List<Handler> {
        return buildList {
            callbackHandler<MainMenuMessageState, CreateNewRangeButton> {
                telegramApi.answerCallbackQuery(callbackQuery.id)
                userStateService.persist(userState.copy(state = TrackGlobalState.NEW_RANGE))
            }
            callbackHandler<MainMenuMessageState, ListRangesButton> {
                telegramApi.answerCallbackQuery(callbackQuery.id)

                val render = rangeList.render(userState)
                statefulMessagesService.editStatefulMessage(userState.chatId, message.messageId, render)
            }
        }
    }
}



@Component
class RangeList(
        @Lazy private val mainMenu: MainMenu
) : HandlerFactory {

    fun render(userState: TrackUserState): StatefulMessageRender {

    }

    override fun handlers(): List<Handler> {
        return buildList {

        }
    }
}

