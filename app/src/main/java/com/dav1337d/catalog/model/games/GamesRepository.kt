package com.dav1337d.catalog.model.games

import android.graphics.Bitmap
import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.StringRequest
import com.dav1337d.catalog.App
import com.dav1337d.catalog.db.AppDatabase
import com.dav1337d.catalog.db.RoomGame
import com.dav1337d.catalog.db.RoomGameDao
import com.dav1337d.catalog.util.ImageSaver
import com.dav1337d.catalog.util.Singletons
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.runBlocking

class GamesRepository {

    var roomGameDao: RoomGameDao = AppDatabase.getInstance(App.appContext!!).roomGameDao()

    val allRoomGames: LiveData<List<RoomGame>> = roomGameDao
        .getAll()

    private var accessToken: String? = null
    private val gson = Gson()

    val searchResults: MutableLiveData<List<GameDetailsResponse>> = MutableLiveData()

    fun getAccessToken() {
        val url =
            "https://id.twitch.tv/oauth2/token?client_id=tgmc1oc8qhc0f8h1lczzsivyjmt7c9&client_secret=8br53h251vo1fx52qdtgciw34mj8i0&grant_type=client_credentials"

        val listener = Response.Listener<String> {
            val result = gson.fromJson(it, GamesAuthenticationResponse::class.java)
            accessToken = result.access_token
        }

        val stringRequest = StringRequest(
            Request.Method.POST, url, listener,
            Response.ErrorListener { Log.i("hallo", "Response Twitch API") })
        val context = App.appContext
        Singletons.getInstance(context!!).addToRequestQueue(stringRequest)
    }

    fun insert(game: GameDetailsResponse, rating: Int, watchDate: String, comment: String) {
//        val listenerImage = Response.Listener<Bitmap> { img ->
//            val fileName = (seriesMovie.original_name + ".png").replace("/", "")
//            ImageSaver(App.appContext!!).setFileName(fileName).setDirectoryName("images").save(img)
//        }
//        getPosterImage("w185", seriesMovie.poster_path, listenerImage)

        roomGameDao.insertAll(game.toRoomEntity(rating, watchDate, comment))
    }

    fun delete(roomGame: RoomGame) {
        //   val fileName = (roomSeriesMovie.original_name + ".png").replace("/", "")
        //    ImageSaver(App.appContext!!).setFileName(fileName).setDirectoryName("images").deleteFile()
        roomGameDao.delete(roomGame)
    }

    fun contains(id: Int): Boolean {
        return runBlocking {
            if (roomGameDao.count(id) > 0) {
                return@runBlocking true
            }
            return@runBlocking false
        }
    }

    fun searchGames(query: String) {
        val url = "https://api.igdb.com/v4/games"

        val request: StringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener { response ->
                if (response != null) {
                    //     Log.i("hallo Games Response", response)
                    try {
                        val result = gson.fromJson(response, Array<GameDetailsResponse>::class.java)

                        result
                            .filter { it.cover != null }
                            .forEach {

                                it.played = contains(it.id)

                                val imageListener = Response.Listener<Bitmap> { img ->
                                    val fileName = (it.name + ".png").replace("/", "")
                                    ImageSaver(App.appContext!!).setFileName(fileName)
                                        .setDirectoryName("images").save(img)

                                    searchResults.value = result.filter { it.cover != null }.toList()
                                }
                                val imageUrl = "https:" + it.cover.url
                                val imageRequest = ImageRequest(imageUrl,
                                    imageListener,
                                    900, 900, ImageView.ScaleType.CENTER, Bitmap.Config.RGB_565,
                                    Response.ErrorListener {
                                        Log.i("hallo img error", it.message)
                                    })
                                Singletons.getInstance(App.appContext!!)
                                    .addToRequestQueue(imageRequest)
                            }

                    } catch (e: JsonSyntaxException) {
                        Log.i("GamesRepository Error", e.message)
                        throw e
                    }

                } else {
                    Log.i("hallo Games Response", "Data Null")
                }
            },
            Response.ErrorListener { error ->
                Log.e("error is ", "" + error)
            }) {
            //This is for Headers If You Needed
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] =
                    "Bearer $accessToken"
                params["Client-ID"] = "tgmc1oc8qhc0f8h1lczzsivyjmt7c9"
                params["Accept"] = "application/json"
                return params
            }

            override fun getBody(): ByteArray {
                // if we change the query (i.e. the fields) we have to adapt the response data class
                val body = "search \"$query\"; fields *, cover.*;"
                return body.toByteArray()
            }
        }
        Singletons.getInstance(App.appContext!!).addToRequestQueue(request)
    }
}