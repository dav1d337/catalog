package com.dav1337d.catalog.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "roomBook", indices = arrayOf(Index(value = ["title"], unique = true)))
data class RoomBook(
    @PrimaryKey val uid: Int? = null,
  //  @ColumnInfo(name = "book") val book: BookItem,
    @ColumnInfo(name = "googleId") val googleId: String,
    @ColumnInfo(name = "kind") val kind: String?,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "subtitle") val subtitle: String?,
    @ColumnInfo(name = "year") val year: String?,
    @ColumnInfo(name = "averageRating") val averageRating: Double?,
    @ColumnInfo(name = "personalRating") val personalRating: Int,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "readDate") val readDate: String?,
    @ColumnInfo(name = "comment") val comment: String?
  //  @ColumnInfo(name = "status") val status:Status?
)

enum class BookStatus {
    FAVORITE, PURCHASED, TO_READ, READING_NOW, HAVE_READ, REVIEWED
}