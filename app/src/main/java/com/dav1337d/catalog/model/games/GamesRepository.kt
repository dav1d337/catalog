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
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.runBlocking
import java.util.*
import kotlin.collections.HashMap


class GamesRepository {

    var roomGameDao: RoomGameDao = AppDatabase.getInstance(App.appContext!!).roomGameDao()

    val allRoomGames: LiveData<List<RoomGame>> = roomGameDao
        .getAll()

    private var accessToken: String? = null
    private val gson = Gson()
    private var firestoreDb: FirebaseFirestore = Firebase.firestore

    val searchResults: MutableLiveData<List<GameDetailsResponse>> = MutableLiveData()
    val gameDetailResult: MutableLiveData<GameDetailsResponse> = MutableLiveData()

    val loading: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)

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

    fun insert(game: GameDetailsResponse, rating: Long, playDate: String, comment: String) {
//        val listenerImage = Response.Listener<Bitmap> { img ->
//            val fileName = (seriesMovie.original_name + ".png").replace("/", "")
//            ImageSaver(App.appContext!!).setFileName(fileName).setDirectoryName("images").save(img)
//        }
//        getPosterImage("w185", seriesMovie.poster_path, listenerImage)
        roomGameDao.insertAll(game.toRoomEntity(rating, playDate, comment))
        addToFirestore(game, rating, playDate, comment)
    }

    private fun addToFirestore(
        game: GameDetailsResponse,
        rating: Long,
        playDate: String,
        comment: String
    ) {

        Log.d(TAG, "Logged in as: ${Firebase.auth.currentUser?.email}")

        val fireGame = hashMapOf(
            "title" to game.name,
            "igdb_id" to game.id,
            "summary" to game.summary,
            "url" to game.url,
            "release_date" to game.first_release_date,
            "cover_url" to game.cover.url,
            "rating" to rating,
            "playDate" to playDate,
            "comment" to comment,
            "creator_id" to Firebase.auth.currentUser?.uid
        )


        val itemsRef = firestoreDb.collection("userGames")
        val query = itemsRef
            .whereEqualTo("creator_id", Firebase.auth.currentUser?.uid)
            .whereEqualTo("igdb_id", game.id)

        query.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot?> { task ->
            if (task.isSuccessful) {
                for (document in task.result!!) {
                    Log.i("hallo", itemsRef.document(document.id).toString())
                    itemsRef.document(document.id).set(fireGame)
                }
                if (task.result!!.isEmpty) {
                    firestoreDb.collection("userGames")
                        .add(fireGame)
                        .addOnSuccessListener { documentReference ->
                            Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                        }
                        .addOnFailureListener { e ->
                            Log.d(TAG, "Error adding document", e)
                        }
                }
            } else {
                Log.d(TAG, "Error getting documents: ", task.exception)
            }
        })

    }

    fun getFromFirestore(): Query {
        return firestoreDb.collection("userGames")
            .whereEqualTo("creator_id", Firebase.auth.currentUser?.uid)
    }

    fun delete(roomGame: RoomGame) {
        val fileName = (roomGame.name + ".png").replace("/", "")
        ImageSaver(App.appContext!!).setFileName(fileName).setDirectoryName("images").deleteFile()

        roomGameDao.delete(roomGame)

        if (Firebase.auth.currentUser != null) {
            val itemsRef = firestoreDb.collection("userGames")
            val query = itemsRef
                .whereEqualTo("creator_id", Firebase.auth.currentUser?.uid)
                .whereEqualTo("igdb_id", roomGame.igdb_id)

            query.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot?> { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        itemsRef.document(document.id).delete()
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.exception)
                }
            })
        }
    }

    fun contains(id: Long): Boolean {
        return runBlocking {
            if (roomGameDao.count(id) > 0) {
                return@runBlocking true
            }
            return@runBlocking false
        }
    }

    fun getGameDetails(id: Long) {
        loading.value = true
        val url = "https://api.igdb.com/v4/games"
        Log.i(TAG, "id: ${id.toString()} token: $accessToken"  )
        val request: StringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener { response ->
                if (response != null) {
                    try {
                        val result = gson.fromJson(response, Array<GameDetailsResponse>::class.java)
                        gameDetailResult.value = result[0]

                        if (!result[0].artworks.isNullOrEmpty()) {
                            val imageListener = Response.Listener<Bitmap> { img ->
                                val fileName = (result[0].name + "backdrop" + ".png").replace("/", "")
                                ImageSaver(App.appContext!!).setFileName(fileName)
                                    .setDirectoryName("images").save(img)
                                loading.value = false
                            }
                            val imageUrl = "https:" + result[0].artworks[0].url.replace("t_thumb", "t_720p")
                            val imageRequest = ImageRequest(imageUrl,
                                imageListener,
                                900, 900, ImageView.ScaleType.CENTER, Bitmap.Config.RGB_565,
                                Response.ErrorListener {
                                    Log.i("hallo img error", it.message)
                                })
                            Singletons.getInstance(App.appContext!!)
                                .addToRequestQueue(imageRequest)
                        } else {
                            loading.value = false
                        }

                    } catch (e: JsonSyntaxException) {
                        Log.i(TAG, e.message)
                        throw e
                    }

                } else {
                    Log.i(TAG, "Data Null")
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
                val body = "fields *, cover.*, artworks.*; where id = $id;"
                return body.toByteArray()
            }
        }
        Singletons.getInstance(App.appContext!!).addToRequestQueue(request)
    }

    fun searchGames(query: String) {
        if (query.isNullOrEmpty()) {
            loading.postValue(false)
        } else {
            loading.postValue(true)
        }
        val url = "https://api.igdb.com/v4/games"

        val request: StringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener { response ->
                if (response != null) {
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

                                    searchResults.value =
                                        result.filter { it.cover != null }.toList()
                                    loading.postValue(false)
                                }
                                val imageUrl = "https:" + it.cover.url.replace("t_thumb", "t_cover_big")
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
                val body = "search \"$query\"; fields *, cover.*, artworks.*;"
                return body.toByteArray()
            }
        }
        Singletons.getInstance(App.appContext!!).addToRequestQueue(request)
    }

    fun clearSearchResults() {
        searchResults.value = listOf()
    }

    companion object {
        const val TAG = "GamesRepository"
    }
}