package kinf.koch.catalog.ui.tv

import kinf.koch.catalog.db.RoomSeriesMovie
import kinf.koch.catalog.model.tv.EitherMovieOrSeries

interface OnClickListener {
    fun onCheckBoxClick(item: EitherMovieOrSeries) { }
    fun onSaveClick(item: EitherMovieOrSeries, rating: Int,  watchDate:String, comment: String) { }
    fun onLongPress(item: RoomSeriesMovie) { }
}