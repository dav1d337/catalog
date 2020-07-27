package kinf.koch.catalog.model.tv

import android.graphics.Bitmap
import android.util.Log
import android.widget.ImageView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.StringRequest
import kinf.koch.catalog.util.Singletons
import kinf.koch.catalog.ui.App
import org.json.JSONObject

class TVRepository {

    fun searchTMB(query: String, listener: Response.Listener<String>) {
        val url =
            "https://api.themoviedb.org/3/search/multi?api_key=0502e000edfc354b128cf682348660a3&query=$query&include_adult=true"

        val stringRequest = StringRequest(
            Request.Method.GET, url,
            listener,
            Response.ErrorListener { Log.i("hallo", "Response Error TMDB") })
        val context = App.getAppContext()
        Singletons.getInstance(context).addToRequestQueue(stringRequest)
    }

    fun handleResponse(response: String): List<EitherMovieOrSeries> {
        val list = mutableListOf<EitherMovieOrSeries>()
        val results = JSONObject(response).getJSONArray("results")
        for (i in 0 until results.length()) {
            val result = results.getJSONObject(i)
            Log.i("hallo", "response: $result")
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

                list.add(EitherMovieOrSeries(
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
                ))
            } else if (results.getJSONObject(i).getString("media_type").equals("movie")) {

                val original_name = result.getString("original_title")
                val name = result.getString("title")
                val release_date = result.getString("release_date")
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
                    list.add(EitherMovieOrSeries(
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
                    ))
                }
            }
        }
        return list
    }

    fun getPosterImage(id: String, listener: Response.Listener<Bitmap>) {
        val url = "https://image.tmdb.org/t/p/w154$id"
        var image: Bitmap? = null

        val imageRequest = ImageRequest(url,
/*            Response.Listener<Bitmap> {
                image = it
                Log.i("hallo", "Response Image OK 1")
            },*/
            listener,
            300, 300, ImageView.ScaleType.CENTER, Bitmap.Config.RGB_565,
            Response.ErrorListener { Log.i("hallo", "Response Error IMAGE 1") })
        val imageLoader =  Singletons.getInstance(App.getAppContext()).imageLoader
        Singletons.getInstance(App.getAppContext()).addToRequestQueue(imageRequest)


        /*imageLoader.get(url,
            object : ImageLoader.ImageListener {
            override fun onResponse(response: ImageLoader.ImageContainer?, isImmediate: Boolean) {
                Log.i("hallo", "Response Image OK 2")
            }

            override fun onErrorResponse(error: VolleyError?) {
                Log.i("hallo", "Response Error IMAGE 2")
            }
        })*/
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