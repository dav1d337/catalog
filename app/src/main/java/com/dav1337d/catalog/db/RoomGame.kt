package com.dav1337d.catalog.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "roomGame", indices = arrayOf(Index(value = ["name"], unique = true)))
data class RoomGame(
    @PrimaryKey val uid: Int? = null,
    @ColumnInfo(name = "igdb_id") val igdb_id: Long,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "summary") val summary: String? = "",
    @ColumnInfo(name = "url") val url: String?,
    @ColumnInfo(name = "first_release_date") val first_release_date: Long?,
    @ColumnInfo(name = "personalRating") val personalRating: Long,
    @ColumnInfo(name = "playDate") val playDate: String?,
    @ColumnInfo(name = "comment") val comment: String?
)