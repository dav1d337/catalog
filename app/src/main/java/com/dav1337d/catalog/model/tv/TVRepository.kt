package com.dav1337d.catalog.model.tv

import android.graphics.Bitmap
import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.LiveData
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.StringRequest
import com.dav1337d.catalog.db.AppDatabase
import com.dav1337d.catalog.db.RoomSeriesDao
import com.dav1337d.catalog.db.RoomSeriesMovie
import com.dav1337d.catalog.ui.App
import com.dav1337d.catalog.util.ImageSaver
import com.dav1337d.catalog.util.Singletons
import org.json.JSONException
import org.json.JSONObject

class TVRepository() {

    var roomSeriesDao: RoomSeriesDao = AppDatabase.getInstance(App.appContext!!).roomSeriesDao()
    val allSeriesMovie: LiveData<List<RoomSeriesMovie>> = roomSeriesDao
        .getAll()


    fun insert(seriesMovie: EitherMovieOrSeries, rating: Int, watchDate: String, comment: String) {
        val listenerImage = Response.Listener<Bitmap> { img ->
            val fileName = (seriesMovie.original_name + ".png").replace("/","")
            ImageSaver(App.appContext!!).setFileName(fileName).setDirectoryName("images").save(img)
        }
        getPosterImage("w185", seriesMovie.poster_path, listenerImage)

        roomSeriesDao.insertAll(seriesMovie.toRoomEntity(rating, watchDate, comment))
    }

    fun delete(roomSeriesMovie: RoomSeriesMovie) {
        val fileName = (roomSeriesMovie.original_name + ".png").replace("/","")
        ImageSaver(App.appContext!!).setFileName(fileName).setDirectoryName("images").deleteFile()
        roomSeriesDao.delete(roomSeriesMovie)
    }

    fun searchTMB(query: String, listener: Response.Listener<String>) {
        val url =
            "https://api.themoviedb.org/3/search/multi?api_key=0502e000edfc354b128cf682348660a3&query=$query&include_adult=true"

        val stringRequest = StringRequest(
            Request.Method.GET, url,
            listener,
            Response.ErrorListener { Log.i("hallo", "Response Error TMDB") })
        val context = App.appContext
        Singletons.getInstance(context!!).addToRequestQueue(stringRequest)
    }

    fun handleResponse(response: String): List<EitherMovieOrSeries> {
        val list = mutableListOf<EitherMovieOrSeries>()
        val results = JSONObject(response).getJSONArray("results")
        for (i in 0 until results.length()) {
            val result = results.getJSONObject(i)
            //    Log.i("hallo", "response: $result")
            if (result.getString("media_type") == "tv") {
                // Series
                val original_name = result.getString("original_name")
                val name = result.getString("name")
                val first_air_date = result.getString("first_air_date")
                val overview = result.getString("overview")
                val rating_tmdb = result.getDouble("vote_average")
                val backdrop_path = result.getString("backdrop_path")
                val poster_path = result.getString("poster_path")
                val id_tmdb = result.getInt("id")
                val result_genres = result.getJSONArray("genre_ids")
                val genres = mutableListOf<Genre>()
                for (j in 0 until result_genres.length()) {
                    genres.add(genreIdToGenre(result_genres[j].toString().toInt()))
                }

                list.add(
                    EitherMovieOrSeries(
                        TypeOfWatchable.SERIES,
                        original_name,
                        name,
                        genres,
                        first_air_date,
                        overview,
                        rating_tmdb,
                        id_tmdb,
                        backdrop_path,
                        poster_path
                    )
                )
            } else if (results.getJSONObject(i).getString("media_type").equals("movie")) {
                try {
                    val original_name = result.getString("original_title")
                    val name = result.getString("title")
                    val release_date = result.getString("release_date") ?: "unkown"
                    val overview = result.getString("overview")
                    val rating_tmdb = result.getDouble("vote_average")
                    val backdrop_path = result.getString("backdrop_path")
                    val poster_path = result.getString("poster_path")
                    val id_tmdb = result.getInt("id")
                    val result_genres = result.getJSONArray("genre_ids")
                    val genres = mutableListOf<Genre>()

                    for (j in 0 until result_genres.length()) {
                        genres.add(genreIdToGenre(result_genres[j].toString().toInt()))
                    }

                    if (!release_date.isNullOrEmpty() && !poster_path.isNullOrEmpty()) {
                        list.add(
                            EitherMovieOrSeries(
                                TypeOfWatchable.MOVIE,
                                original_name,
                                name,
                                genres,
                                release_date,
                                overview,
                                rating_tmdb,
                                id_tmdb,
                                backdrop_path,
                                poster_path
                            )
                        )
                    }
                } catch (e: JSONException) {
                    // TODO: handle
                }

            }
        }
        return list
    }

    fun getPosterImage(size: String = "w154", id: String, listener: Response.Listener<Bitmap>) {
        if (id != null) {
            val url = "https://image.tmdb.org/t/p/$size$id"
            val imageRequest = ImageRequest(url,
                listener,
                300, 300, ImageView.ScaleType.CENTER, Bitmap.Config.RGB_565,
                Response.ErrorListener { Log.i("hallo", "Response Error IMAGE 1") })
            Singletons.getInstance(App.appContext!!).addToRequestQueue(imageRequest)
        }
    }

    companion object {
        fun genreIdToGenre(code: Int): Genre {
            return when (code) {
                28 -> Genre("Action", 28)
                12 -> Genre("Adventure", 12)
                16 -> Genre("Animation", 16)
                35 -> Genre("Comedy", 35)
                80 -> Genre("Crime", 80)
                99 -> Genre("Documentary", 99)
                18 -> Genre("Drama", 18)
                10751 -> Genre("Family", 10751)
                14 -> Genre("Fantasy", 14)
                36 -> Genre("History", 36)
                27 -> Genre("Horror", 27)
                10402 -> Genre("Music", 10402)
                9648 -> Genre("Mystery", 9648)
                10749 -> Genre("Romance", 10749)
                878 -> Genre("Science Fiction", 878)
                10770 -> Genre("TV Movie", 10770)
                53 -> Genre("Thriller", 53)
                10752 -> Genre("War", 10752)
                37 -> Genre("Western", 37)
                else -> Genre("Other", 1337)
            }
        }
    }
}