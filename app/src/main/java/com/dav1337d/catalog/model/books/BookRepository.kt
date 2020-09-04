package com.dav1337d.catalog.model.books

import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.dav1337d.catalog.ui.App
import com.dav1337d.catalog.util.Singletons
import com.google.gson.Gson
import com.google.gson.JsonParser
import org.json.JSONArray
import org.json.JSONObject

class BookRepository {

    fun searchBook(query: String) {
        val url =
            "https://www.googleapis.com/books/v1/volumes?q=flowers+inauthor:keyes&key=AIzaSyAQUsp3YZkeorxwt_WRzcTYl1p9xHKTBD0"

        val listener = Response.Listener<String> {
            val gson = Gson()

            val result = gson.fromJson(it, BookSearchResponse::class.java)

            Log.i("hallo result", result.toString())
        }
        val stringRequest = StringRequest(
            Request.Method.GET, url, listener,
            Response.ErrorListener { Log.i("hallo", "Response Error Google Books") })
        val context = App.appContext
        Singletons.getInstance(context!!).addToRequestQueue(stringRequest)
    }
}