package com.dav1337d.catalog.model.tv

import android.graphics.Bitmap
import com.dav1337d.catalog.db.RoomSeriesMovie

enum class TypeOfWatchable { MOVIE, SERIES }

data class EitherMovieOrSeries(
    val type: TypeOfWatchable,
    val original_name: String,
    val name: String,
    val genres: List<Genre>,
    val first_air_date: String,
    val overview: String,
    val rating_tmdb: Double,
    val id_tmdb: Int,
    val backdrop_path: String,
    val poster_path: String,
    var poster: Bitmap? = null,
    var watched: Boolean? = false
) {
    fun toRoomEntity(personalRating: Long, watchDate: String, comment: String): RoomSeriesMovie {
        return RoomSeriesMovie(
            //      this.type,
            this.id_tmdb,
            this.original_name,
            this.name,
            this.first_air_date,
            this.overview,
            this.rating_tmdb,
            this.id_tmdb,
            this.backdrop_path,
            this.poster_path,
            personalRating,
            watchDate,
            comment
        )
    }
}
