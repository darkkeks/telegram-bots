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

    override fun update() {
        val sources = getSources()
                .filterIsInstance<PlaylistSource>()

        val playlistIds = sources
                .map { it.playlist }
                .distinct()

        val results = playlistIds.map { id ->
            val response = youTube.PlaylistItems()
                    .list("id,snippet")
                    .apply {
                        maxResults = 100
                        playlistId = id
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

            id to Playlist(id, videos)
        }.toMap()

        sources.forEach { source ->
            val result = results[source.playlist]
            if (result != null) {
                putSourceInfo(source, result)
            }
        }
    }

    private fun toMoscowLocalDateTime(dateTime: DateTime): LocalDateTime {
        val utc = ZonedDateTime.parse(dateTime.toStringRfc3339())
        return utc.withZoneSameInstant(moscowZoneId).toLocalDateTime()
    }
}
