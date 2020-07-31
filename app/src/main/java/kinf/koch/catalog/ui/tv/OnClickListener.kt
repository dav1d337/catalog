package kinf.koch.catalog.ui.tv

import kinf.koch.catalog.model.tv.EitherMovieOrSeries

interface OnClickListener {
    fun onCheckBoxClick(item: EitherMovieOrSeries)

}