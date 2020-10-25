package com.dav1337d.catalog.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.dav1337d.catalog.model.books.BookItem

@Entity(tableName = "roomBook")
data class RoomBook(
    @PrimaryKey val uid: Int? = null,
  //  @ColumnInfo(name = "book") val book: BookItem,
    @ColumnInfo(name = "readDate") val readDate: String?,
    @ColumnInfo(name = "comment") val comment: String?
  //  @ColumnInfo(name = "status") val status:Status?
)

enum class Status {
    FAVORITE, PURCHASED, TO_READ, READING_NOW, HAVE_READ, REVIEWED
}