package ru.darkkeks.telegram.hseremind.youtube

import org.springframework.data.repository.CrudRepository
import java.time.LocalDateTime

interface YoutubePlaylistRepository : CrudRepository<Playlist, String>

data class Playlist(
        val id: String,
        val videos: List<Video>
)

data class Video(
        val id: String,
        val title: String,
        val description: String?,
        val publishedAt: LocalDateTime
)
