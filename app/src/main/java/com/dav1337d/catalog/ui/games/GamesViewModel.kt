package com.dav1337d.catalog.ui.games

import android.graphics.Bitmap
import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.volley.Response
import com.android.volley.toolbox.ImageRequest
import com.dav1337d.catalog.App
import com.dav1337d.catalog.db.RoomGame
import com.dav1337d.catalog.model.games.GamesRepository
import com.dav1337d.catalog.util.ImageSaver
import com.dav1337d.catalog.util.Singletons
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GamesViewModel constructor(private val gamesRepository: GamesRepository) : ViewModel() {

    val liveData = MediatorLiveData<List<RoomGame>>()
    private val firebaseAuth = Firebase.auth
    private val firestoreDb = Firebase.firestore

    init {
        if (firebaseAuth.currentUser == null) {
            liveData.addSource(gamesRepository.allRoomGames) {
                if (it != null) {
                    liveData.value = it
                }
            }
        } else {
            // getFirebaseItems()
        }
    }

    private fun getFirebaseItems() {
        firestoreDb.collection("userGames")
            .whereEqualTo("creator_id", Firebase.auth.currentUser?.uid)
            .get()
            .addOnSuccessListener { snapshot ->
                val list = mutableListOf<RoomGame>()
                snapshot.documents.forEach { game ->
                    val roomGame = RoomGame(
                        igdb_id = game.data?.get("igdb_id") as Long,
                        name = game.data?.get("title") as String,
                        summary = game.data?.get("summary") as String,
                        url = game.data?.get("url") as String,
                        first_release_date = game.data?.get("release_date") as Long,
                        personalRating = game.data?.get("rating") as Long,
                        playDate = game.data?.get("playDate") as String,
                        comment = game.data?.get("comment") as String
                    )
                    list.add(roomGame)

                    val imageListener = Response.Listener<Bitmap> { img ->
                        val fileName = (game.data?.get("title") as String + ".png").replace("/", "")
                        ImageSaver(App.appContext!!).setFileName(fileName)
                            .setDirectoryName("images").save(img)
                    }
                    val imageUrl = "https:" + game.data?.get("cover_url") as String
                    val imageRequest = ImageRequest(imageUrl,
                        imageListener,
                        900, 900, ImageView.ScaleType.CENTER, Bitmap.Config.RGB_565,
                        Response.ErrorListener {
                            Log.i("hallo img error", it.message)
                        })
                    Singletons.getInstance(App.appContext!!)
                        .addToRequestQueue(imageRequest)
                }
                liveData.value = list
            }
            .addOnFailureListener { e ->
                Log.d(TAG, "Error getting documents", e)
            }
    }

    fun getGamesFromFirestore(): LiveData<List<RoomGame>> {
        gamesRepository.getFromFirestore()
            .addSnapshotListener(EventListener<QuerySnapshot> { value, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                    liveData.value = null
                    return@EventListener
                }

                val list = mutableListOf<RoomGame>()
                value?.forEach { game ->
                    val roomGame = RoomGame(
                        igdb_id = game.data?.get("igdb_id") as Long,
                        name = game.data?.get("title") as String,
                        summary = game.data?.get("summary") as String,
                        url = game.data?.get("url") as String,
                        first_release_date = game.data?.get("release_date") as Long,
                        personalRating = game.data?.get("rating") as Long,
                        playDate = game.data?.get("playDate") as String,
                        comment = game.data?.get("comment") as String
                    )
                    list.add(roomGame)

                    val imageListener = Response.Listener<Bitmap> { img ->
                        val fileName = (game.data?.get("title") as String + ".png").replace("/", "")
                        ImageSaver(App.appContext!!).setFileName(fileName)
                            .setDirectoryName("images").save(img)
                        liveData.value = liveData.value
                    }
                    val imageUrl = "https:" + game.data?.get("cover_url") as String
                    val imageRequest = ImageRequest(imageUrl,
                        imageListener,
                        900, 900, ImageView.ScaleType.CENTER, Bitmap.Config.RGB_565,
                        Response.ErrorListener {
                            Log.i("hallo img error", it.message)
                        })
                    Singletons.getInstance(App.appContext!!)
                        .addToRequestQueue(imageRequest)
                }

                liveData.value = list
            })

        return liveData
    }

    fun delete(roomGame: RoomGame) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                gamesRepository.delete(roomGame)
            }
        }
    }

    fun sortItemsBy(by: String): Boolean {
        liveData.value?.let { game ->
            when (by) {
                "name" -> {
                    if (game != game.sortedBy { it.name }) {
                        liveData.value = game.sortedBy { it.name }
                    } else {
                        liveData.value = game.sortedByDescending { it.name }
                    }
                }
                "first_air_date" -> {
                    if (game != game.sortedBy { it.first_release_date }) {
                        liveData.value = game.sortedBy { it.first_release_date }
                    } else {
                        liveData.value = game.sortedByDescending { it.first_release_date }
                    }
                }
                "readDate" -> {
                    if (game != game.sortedBy { it.playDate }) {
                        liveData.value = game.sortedBy { it.playDate }
                    } else {
                        liveData.value = game.sortedByDescending { it.playDate }
                    }
                }
                "personalRating" -> {
                    if (game != game.sortedBy { it.personalRating }) {
                        liveData.value = game.sortedBy { it.personalRating }
                    } else {
                        liveData.value = game.sortedByDescending { it.personalRating }
                    }
                }
            }
        }
        return true
    }

    companion object {
        const val TAG = "GamesViewModel"
    }
}
