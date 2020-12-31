package ru.darkkeks.telegram.hseremind.youtube

import org.springframework.stereotype.Component
import ru.darkkeks.telegram.core.api.ParseMode
import ru.darkkeks.telegram.core.api.Telegram
import ru.darkkeks.telegram.core.api.TelegramClientException
import ru.darkkeks.telegram.core.createLogger
import ru.darkkeks.telegram.hseremind.ChatSpec
import ru.darkkeks.telegram.hseremind.ItemFilterService
import ru.darkkeks.telegram.hseremind.User
import ru.darkkeks.telegram.hseremind.UserRepository
import ru.darkkeks.telegram.hseremind.safeParseSpec
import ru.darkkeks.telegram.hseremind.targetToChatId

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
        val video: Video,
        val playlistSource: PlaylistSource
    )

    fun update() = try {
        logger.info("Starting notify iteration")

        val notifications = userRepository.findAll().flatMap { user ->
            val spec = safeParseSpec(user.spec)
            spec?.chats?.flatMap { chat ->
                chat.youtubeRules?.flatMap { rule ->
                    processRule(user, chat, rule)
                } ?: listOf()
            } ?: listOf()
        }

        notifications
            .sortedBy { it.video.publishedAt }
            .forEach { sendNotification(it.name, it.chatId, it.video, it.playlistSource) }

        logger.info("Done")
    } catch (e: Exception) {
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
                    items.add(NotifyItem(playlistSource.name, chatId, video, playlistSource))
                }
            }
        }

        youtubePlaylistRepository.save(newPlaylist)
        return items
    }

    private fun sendNotification(name: String, chatId: Long, video: Video, playlistSource: PlaylistSource) {
        val items = video.title.split(""",\s*""".toRegex()).joinToString("\n")
        var text = items + "\n"
        text += "https://youtube.com/watch?v=${video.id}&list=${playlistSource.playlist}\n"
        text += extractTags(name, video.title).joinToString(" ")

        logger.info("Sending notification:\n{}", text)

        try {
            telegram.sendMessage(chatId, text, parseMode = ParseMode.HTML, disableWebPagePreview = true)
        } catch (e: TelegramClientException) {
            logger.warn("Failed to send notification to chat {}", chatId, e)
        }

        Thread.sleep(4000)
    }

    private fun extractTags(name: String, title: String): List<String> {
        val tags = buildList {
            add(name)

            val groupRegex = """19\d{1,2}""".toRegex()
            val match = groupRegex.find(title)
            if (match != null) {
                add("Г" + match.value)
                add(name + "_" + match.value)
            }

            if (title.contains("лекция", ignoreCase = true)
                || title.contains("lect", ignoreCase = true)
            ) {
                add(name + "_Л")
            }
        }
        return tags.map { "#$it" }
    }
}
