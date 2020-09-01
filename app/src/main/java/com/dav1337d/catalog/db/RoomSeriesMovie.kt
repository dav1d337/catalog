package com.dav1337d.catalog.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "roomSeriesMovie", indices = arrayOf(Index(value = ["original_name"], unique = true)))
data class RoomSeriesMovie(
    @PrimaryKey val uid: Int? = null,
 //   @ColumnInfo(name = "type") val type: TypeOfWatchable,
    @ColumnInfo(name = "original_name") val original_name: String,
    @ColumnInfo(name = "name") val name: String,
  //  @ColumnInfo(name = "genres") val genres: List<Genre>,
    @ColumnInfo(name = "first_air_date") val first_air_date: String,
    @ColumnInfo(name = "overview") val overview: String,
    @ColumnInfo(name = "rating_tmdb") val rating_tmdb: Double,
    @ColumnInfo(name = "id_tmdb") val id_tmdb: Int,
    @ColumnInfo(name = "backdrop_path") val backdrop_path: String,
    @ColumnInfo(name = "poster_path") val poster_path: String,
    @ColumnInfo(name = "personalRating") val personalRating: Int,
    @ColumnInfo(name = "watchDate") val watchDate: String?,
    @ColumnInfo(name = "comment") val comment: String?
)