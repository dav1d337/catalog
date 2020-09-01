package com.dav1337d.catalog.ui.tv

import com.dav1337d.catalog.db.RoomSeriesMovie
import com.dav1337d.catalog.model.tv.EitherMovieOrSeries

interface OnClickListener {
    fun onCheckBoxClick(item: EitherMovieOrSeries) { }
    fun onSaveClick(item: EitherMovieOrSeries, rating: Int,  watchDate:String, comment: String) { }
    fun onLongPress(item: RoomSeriesMovie) { }
}