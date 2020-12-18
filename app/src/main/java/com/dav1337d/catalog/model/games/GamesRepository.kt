package com.dav1337d.catalog.model.games

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.dav1337d.catalog.App
import com.dav1337d.catalog.util.Singletons
import com.google.gson.Gson

class GamesRepository {

    private var accessToken: String? = null
    private val gson = Gson()

    val searchResults: MutableLiveData<List<Game>> = MutableLiveData(mutableListOf())

    fun getAccessToken() {
        val url = "https://id.twitch.tv/oauth2/token?client_id=tgmc1oc8qhc0f8h1lczzsivyjmt7c9&client_secret=8br53h251vo1fx52qdtgciw34mj8i0&grant_type=client_credentials"

        val listener = Response.Listener<String> {
            Log.i("hallo games reponse", it)
            val result = gson.fromJson(it, GamesAuthenticationResponse::class.java)
            accessToken = result.access_token
        }

        val stringRequest = StringRequest(
            Request.Method.POST, url, listener,
            Response.ErrorListener { Log.i("hallo", "Response Twitch API") })
        val context = App.appContext
        Singletons.getInstance(context!!).addToRequestQueue(stringRequest)
    }

    fun searchGames(query: String) {
        val url = "https://api.igdb.com/v4/games"

        val request: StringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener { response ->
                if (response != null) {
                    Log.i("hallo Games Response", response)
                    val result = gson.fromJson(response, Array<SingleGameSearchResponse>::class.java)

                    result.forEach {
                        getGameDetails(it.id)
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
                val body = "search \"$query\"; fields name;"
                return body.toByteArray()
            }
        }

        Singletons.getInstance(App.appContext!!).addToRequestQueue(request)
    }

    private fun getGameDetails(id: Int) {
        val url = "https://api.igdb.com/v4/games"

        val request: StringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener { response ->
                if (response != null) {
                    val result = gson.fromJson(response, Array<GameDetailsResponse>::class.java)

                    searchResults.value?.toMutableList()?.add(Game(result[0].name))
                    Log.i("hallo lol", result[0].name)
                } else {
                    Log.i("hallo GameDetail", "Data Null")
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
                val body = "fields *; where id = $id;"
                return body.toByteArray()
            }
        }
        Singletons.getInstance(App.appContext!!).addToRequestQueue(request)
    }
}