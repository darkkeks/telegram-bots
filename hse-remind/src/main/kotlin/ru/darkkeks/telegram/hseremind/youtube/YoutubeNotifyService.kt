package ru.darkkeks.telegram.hseremind.youtube

import org.springframework.stereotype.Component
import ru.darkkeks.telegram.core.api.ParseMode
import ru.darkkeks.telegram.core.api.Telegram
import ru.darkkeks.telegram.core.api.TelegramClientException
import ru.darkkeks.telegram.core.api.executeChecked
import ru.darkkeks.telegram.core.createLogger
import ru.darkkeks.telegram.hseremind.*

@Component
class YoutubeNotifyService(
        val userRepository: UserRepository,
        val youtubePlaylistRepository: YoutubePlaylistRepository,
        val youtubeSourceFetchService: YoutubeSourceFetchService,
        val itemFilterService: ItemFilterService,
        val telegram: Telegram
) {
    val logger = createLogger<YoutubeNotifyService>()

    data class NotifyItem(
            val name: String,
            val chatId: Long,
            val video: Video
    )

    fun update() = try {
        logger.info("Starting notify iteration")

        val notifications = userRepository.findAll().flatMap { user ->
            user.spec.chats.flatMap { chat ->
                chat.youtubeRules?.flatMap { rule ->
                    processRule(user, chat, rule)
                } ?: listOf()
            }
        }

        notifications
                .sortedBy { it.video.publishedAt }
                .forEach { sendNotification(it.name, it.chatId, it.video) }

        logger.info("Done")
    } catch (e : Exception) {
        logger.warn("Exception during notify iteration", e)
    }

    private fun processRule(user: User, chat: ChatSpec, rule: YouTubeRuleSpec): List<NotifyItem> {
        val playlistSource = rule.source as? PlaylistSource ?: return listOf()
        val playlistId = playlistSource.playlist

        val newPlaylist = youtubeSourceFetchService.getSourceInfo(playlistSource)
        val oldPlaylist = youtubePlaylistRepository.findById(playlistId)

        val oldItems = oldPlaylist.orElse(null)?.videos ?: listOf()
        val newItems = newPlaylist?.videos ?: return listOf()

        val items = mutableListOf<NotifyItem>()
        (newItems - oldItems).forEach { video ->
            if (rule.filter == null || itemFilterService.shouldNotify(video, rule.filter)) {
                val chatId = targetToChatId(user, chat.target)
                if (chatId != null) {
                    items.add(NotifyItem(playlistSource.name, chatId, video))
                }
            }
        }

        youtubePlaylistRepository.save(newPlaylist)
        return items
    }

    private fun sendNotification(name: String, chatId: Long, video: Video) {
        val items = video.title.split(""",\s*""".toRegex()).joinToString("\n")
        var text = items + "\n"
        text += "https://youtube.com/watch?v=${video.id}\n"
        text += "#$name"

        logger.info("Sending notification:\n{}", text)

        try {
            telegram.sendMessage(chatId, text, parseMode = ParseMode.HTML, disableWebPagePreview = true)
                    .executeChecked()
        } catch (e: TelegramClientException) {
            logger.warn("Failed to send notification to chat {}", chatId, e)
        }

        Thread.sleep(4000)
    }

}
