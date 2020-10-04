package ru.darkkeks.telegram.hseremind.youtube

import com.fasterxml.jackson.annotation.JsonSubTypes
import ru.darkkeks.telegram.hseremind.*

data class YouTubeRuleSpec(
        val source: YouTubeSource,
        val filter: YoutubeFilter?
)

@JsonSubTypes(
        JsonSubTypes.Type(value = PlaylistSource::class, name = "playlist")
)
abstract class YouTubeSource : Source()

data class PlaylistSource(val name: String, val playlist: String) : YouTubeSource()

@JsonSubTypes(
        JsonSubTypes.Type(value = TitleFilter::class, name = "title")
)
abstract class YoutubeFilter : Filter()

data class TitleFilter(val title: String) : YoutubeFilter()
