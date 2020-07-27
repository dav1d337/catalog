package kinf.koch.catalog.model.tv

import android.graphics.Bitmap

enum class TypeOfWatchable { MOVIE, SERIES}

data class EitherMovieOrSeries(val type: TypeOfWatchable, val original_name: String, val name: String, val genres: List<Genre>, val first_air_date: String, val overview: String, val rating_tmdb: Double, val id_tmdb: Int, val backdrop_path: String, val poster_path: String, var poster: Bitmap? = null)