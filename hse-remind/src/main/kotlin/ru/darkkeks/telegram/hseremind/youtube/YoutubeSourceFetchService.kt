package ru.darkkeks.telegram.hseremind.youtube

import com.google.api.client.util.DateTime
import com.google.api.services.youtube.YouTube
import org.springframework.stereotype.Component
import ru.darkkeks.telegram.core.createLogger
import ru.darkkeks.telegram.hseremind.AbstractSourceFetchService
import ru.darkkeks.telegram.hseremind.ruz.RuzUtils.moscowZoneId
import java.time.LocalDateTime
import java.time.ZonedDateTime

@Component
class YoutubeSourceFetchService(
        val youTube: YouTube
) : AbstractSourceFetchService<YouTubeSource, Playlist>() {
    val logger = createLogger<YoutubeSourceFetchService>()

    override val sourceType = YouTubeSource::class

    data class PageInfo(
            val videos: List<Video>,
            val pageToken: String?
    )

    override fun update() {
        val sources = getSources()
                .filterIsInstance<PlaylistSource>()

        val playlistIds = sources
                .map { it.playlist }
                .distinct()

        logger.info("Fetching {} playlist sources", playlistIds.size)

        val results = playlistIds.map { id ->
            val result = mutableListOf<Video>()

            var pageToken: String? = null
            while (true) {
                val page = getPlaylistPage(id, pageToken)
                result += page.videos
                pageToken = page.pageToken
                if (pageToken == null) {
                    break
                }
            }

            id to Playlist(id, result)
        }.toMap()

        logger.info("Fetched results for {} playlists", results.size)

        sources.forEach { source ->
            val result = results[source.playlist]
            if (result != null) {
                putSourceInfo(source, result)
            }
        }
    }

    private fun getPlaylistPage(id: String, page: String? = null): PageInfo {
        val response = youTube.PlaylistItems()
                .list("id,snippet")
                .apply {
                    maxResults = 50
                    playlistId = id
                    pageToken = page
                }
                .execute()

        val videos = response.items
                .asSequence()
                .filter { it.kind == "youtube#playlistItem" }
                .map { it.snippet }
                .filter { it.resourceId.kind == "youtube#video" }
                .sortedBy { it.position }
                .map {
                    Video(
                            id = it.resourceId.videoId,
                            title = it.title,
                            description = it.description,
                            publishedAt = toMoscowLocalDateTime(it.publishedAt)
                    )
                }
                .toList()

        return PageInfo(videos, response.nextPageToken)
    }

    private fun toMoscowLocalDateTime(dateTime: DateTime): LocalDateTime {
        val utc = ZonedDateTime.parse(dateTime.toStringRfc3339())
        return utc.withZoneSameInstant(moscowZoneId).toLocalDateTime()
    }
}
