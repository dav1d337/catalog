package com.dav1337d.catalog.model.games

import android.graphics.Bitmap
import com.dav1337d.catalog.db.RoomGame

data class GameDetailsResponse constructor(
    val id: Int,
    val age_ratings: List<Int>,
    val aggregated_rating: Float,
    val aggregated_rating_count: Float,
    val alternative_names: List<Int>,
    val artworks: List<Int>,
    val bundles: List<Int>,
    val category: Int,
    val collection: Int,
    val cover: Cover,
    val created_at: Int,
    val dlcs: List<Int>,
    val external_games: List<Int>,
    val first_release_date: Long,
    val follows: Int,
    val franchises: List<Int>,
    val game_engines: List<Int>,
    val game_modes: List<Int>,
    val genres: List<Int>,
    val hypes: Int,
    val involved_companies: List<Int>,
    val keywords: List<Int>,
    val multiplayer_modes: List<Int>,
    val name: String,
    val platforms: List<Int>,
    val player_perspectives: List<Int>,
    val rating: Float,
    val release_dates: List<Int>,
    val screenshots: List<Int>,
    val similar_games: List<Int>,
    val slug: String,
    val standalone_expansions: List<Int>,
    val storyline: String,
    val summary: String,
    val tags: List<Int>,
    val themes: List<Int>,
    val total_rating: Float,
    val total_rating_count: Int,
    val updated_at: Int,
    val url: String,
    val videos: List<Int>,
    val websites: List<Int>,
    val checksum: String,
    var played: Boolean? = false
) {
    fun toRoomEntity(personalRating: Int, playDate: String, comment: String): RoomGame {
        return RoomGame(
            igdb_id = this.id,
            name = this.name,
            summary = this.summary,
            url = this.url,
            first_release_date = this.first_release_date,
            personalRating = personalRating,
            playDate = playDate,
            comment = comment
        )
    }
}

data class Cover(
    val id: Int,
    val alpha_channel: Boolean,
    val animated: Boolean,
    val game: Int,
    val height: Int,
    val image_id: String,
    val url: String,
    val width: Int,
    val checksum: String
)