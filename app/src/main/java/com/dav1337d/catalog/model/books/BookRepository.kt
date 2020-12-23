package com.dav1337d.catalog.model.books

import android.graphics.Bitmap
import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.LiveData
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.StringRequest
import com.dav1337d.catalog.App
import com.dav1337d.catalog.db.*
import com.dav1337d.catalog.util.ImageSaver
import com.dav1337d.catalog.util.Singletons
import com.google.android.gms.auth.api.signin.GoogleSignIn
import kotlinx.coroutines.runBlocking


class BookRepository {

    var roomBookDao: RoomBookDao = AppDatabase.getInstance(App.appContext!!).roomBookDao()
    val allBooks: LiveData<List<RoomBook>> = roomBookDao
        .getAll()

    fun searchBook(query: String, listener: Response.Listener<String>) {
        val url =
            "https://www.googleapis.com/books/v1/volumes?q=$query&key=AIzaSyAQUsp3YZkeorxwt_WRzcTYl1p9xHKTBD0"


        val stringRequest = StringRequest(
            Request.Method.GET, url, listener,
            Response.ErrorListener { Log.i("hallo", "Response Error Google Books") })
        val context = App.appContext
        Singletons.getInstance(context!!).addToRequestQueue(stringRequest)
    }

    fun getBookImage(item: BookItem, listenerImage: Response.Listener<Bitmap>?) {
        val url = item.volumeInfo.imageLinks!!.get("thumbnail")?.replaceFirst("http", "https")
        val imageRequest = ImageRequest(url,
            listenerImage,
            300, 300, ImageView.ScaleType.CENTER, Bitmap.Config.RGB_565,
            Response.ErrorListener { Log.i("BookRepo Image" + "hallo", it.message) })
        Singletons.getInstance(App.appContext!!).addToRequestQueue(imageRequest)
    }

    fun insert(
        item: BookItem, rating: Int, readDate: String, comment: String//, status: BookStatus
    ) {
        roomBookDao.insertAll(
            RoomBook(
                googleId = item.id,
                kind = item.kind,
                title = item.volumeInfo.title,
                subtitle = item.volumeInfo.subtitle,
                year = item.volumeInfo.publishedDate,
                averageRating = item.volumeInfo.averageRating,
                personalRating = rating,
                description = item.volumeInfo.description,
                readDate = readDate,
                comment = comment
            )
        )
        val listenerImage = Response.Listener<Bitmap> { img ->
            val fileName = (item.volumeInfo.title + ".png").replace("/", "")
            ImageSaver(App.appContext!!).setFileName(fileName).setDirectoryName("images").save(img)
        }
        getBookImage(item, listenerImage)
    }

    fun addBookToBookshelf(item: String? = "r7rbDwAAQBAJ", bookshelfId: Int? = 0) {

        val account = GoogleSignIn.getLastSignedInAccount(App.appContext)
        Log.i("hallo BookRepository", "account auth code" + account?.idToken)


        val url =
            "https://www.googleapis.com/books/v1/mylibrary/bookshelves/$bookshelfId/addVolume?volumeId=${item}&key=AIzaSyAQUsp3YZkeorxwt_WRzcTYl1p9xHKTBD0"
        val request: StringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener { response ->
                if (response != null) {
                    Log.e("Your Array Response", response)
                } else {
                    Log.e("Your Array Response", "Data Null")
                }
            },
            Response.ErrorListener { error -> Log.e("error is ", "" + error) }) {
            //This is for Headers If You Needed
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] =
                    "Bearer " + "ya29.a0AfH6SMAyqJB4UZ9eWOiB1-aFoNsvRF22wqJdLZ1CY4LU0dmeuE7123v2AJ_X08n5hjx9vwBrA5EtchruFAr6llqL--9jf48v5I94Ur1FWCIAypGoSNVXO4npenZgU8q918IO5s2UYJGhFZktkwbY0LSVUwXrczHa5W0"
                params["Content-Type"] = "application/json"
                params["Content-Length"] = "0"
                return params
            }
        }

        Singletons.getInstance(App.appContext!!).addToRequestQueue(request)
    }


    fun delete(roomBook: RoomBook) {
        //   val fileName = (roomSeriesMovie.original_name + ".png").replace("/", "")
        //    ImageSaver(App.appContext!!).setFileName(fileName).setDirectoryName("images").deleteFile()
        roomBookDao.delete(roomBook)
    }

    fun contains(id: String): Boolean {
        return runBlocking {
            if (roomBookDao.count(id) > 0) {
                return@runBlocking true
            }
            return@runBlocking false
        }
    }
}