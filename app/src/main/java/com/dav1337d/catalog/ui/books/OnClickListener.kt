package com.dav1337d.catalog.ui.books

import com.dav1337d.catalog.db.RoomBook
import com.dav1337d.catalog.db.RoomSeriesMovie
import com.dav1337d.catalog.model.books.BookItem
import com.dav1337d.catalog.model.tv.EitherMovieOrSeries

interface OnClickListener {
    fun onCheckBoxClick(item: BookItem) { }
    fun onSaveClick(item: BookItem, rating: Int, watchDate:String, comment: String) { }
    fun onLongPress(item: RoomBook) { }
}