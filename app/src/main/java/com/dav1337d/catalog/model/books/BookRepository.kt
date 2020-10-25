package com.dav1337d.catalog.model.books

import android.accounts.AccountManager
import android.accounts.AccountManagerCallback
import android.accounts.AccountManagerFuture
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.dav1337d.catalog.ui.App
import com.dav1337d.catalog.util.Singletons
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth


class BookRepository {

    fun searchBook(query: String, listener: Response.Listener<String>) {
        val url =
            "https://www.googleapis.com/books/v1/volumes?q=$query&key=AIzaSyAQUsp3YZkeorxwt_WRzcTYl1p9xHKTBD0"


        val stringRequest = StringRequest(
            Request.Method.GET, url, listener,
            Response.ErrorListener { Log.i("hallo", "Response Error Google Books") })
        val context = App.appContext
        Singletons.getInstance(context!!).addToRequestQueue(stringRequest)
    }

    fun addBookToBookshelf(item: String? = "r7rbDwAAQBAJ", bookshelfId: Int? = 0) {

        val account = GoogleSignIn.getLastSignedInAccount(App.appContext)
        Log.i("hallo BookRepository", "account auth code" + account?.idToken)


        val url = "https://www.googleapis.com/books/v1/mylibrary/bookshelves/$bookshelfId/addVolume?volumeId=${item}&key=AIzaSyAQUsp3YZkeorxwt_WRzcTYl1p9xHKTBD0"
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
                params["Authorization"] = "Bearer " + "ya29.a0AfH6SMAyqJB4UZ9eWOiB1-aFoNsvRF22wqJdLZ1CY4LU0dmeuE7123v2AJ_X08n5hjx9vwBrA5EtchruFAr6llqL--9jf48v5I94Ur1FWCIAypGoSNVXO4npenZgU8q918IO5s2UYJGhFZktkwbY0LSVUwXrczHa5W0"
                params["Content-Type"] = "application/json"
                params["Content-Length"] = "0"
                return params
            }
        }

        Singletons.getInstance(App.appContext!!).addToRequestQueue(request)
    }
}