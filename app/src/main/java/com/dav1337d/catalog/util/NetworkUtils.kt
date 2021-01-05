package com.dav1337d.catalog.util

import android.content.Context
import android.graphics.Bitmap
import android.util.LruCache
import android.widget.ImageView
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class NetworkUtils constructor(context: Context) {
    companion object {
        @Volatile
        private var INSTANCE: NetworkUtils? = null
        fun getInstance(context: Context) =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: NetworkUtils(context).also {
                        INSTANCE = it
                    }
            }
    }

    /**
     * Use [ImageSaver] instead
     */
    val imageLoader: ImageLoader by lazy {
        ImageLoader(requestQueue,
            object : ImageLoader.ImageCache {
                private val cache = LruCache<String, Bitmap>(20)
                override fun getBitmap(url: String): Bitmap {
                    return cache.get(url)
                }

                override fun putBitmap(url: String, bitmap: Bitmap) {
                    cache.put(url, bitmap)
                }
            })
    }

    private val requestQueue: RequestQueue by lazy {
        // applicationContext is key, it keeps you from leaking the
        // Activity or BroadcastReceiver if someone passes one in.
        Volley.newRequestQueue(context.applicationContext)
    }

    fun createRequestWithHeaderBodyAndAddToQueue(
        url: String,
        type: RequestType,
        method: Int?,
        successListener: Response.Listener<Any>,
        errorListener: Response.ErrorListener,
        params: Map<String, String>,
        body: ByteArray,
        imageOptions: ImageOptions?
    ) {
        when (type) {
            RequestType.STRING -> {
                val request = object : StringRequest(
                    method!!,
                    url,
                    successListener as Response.Listener<String>,
                    errorListener
                ) {
                    @Throws(AuthFailureError::class)
                    override fun getHeaders(): Map<String, String> {
                        return params
                    }

                    override fun getBody(): ByteArray {
                        return body
                    }
                }
                addToRequestQueue(request)
            }
            RequestType.IMAGE -> {
                val request = object : ImageRequest(
                    url,
                    successListener as Response.Listener<Bitmap>,
                    imageOptions!!.maxWidth,
                    imageOptions.maxHeigth,
                    imageOptions.scaleType,
                    imageOptions.config,
                    errorListener
                ) {
                    @Throws(AuthFailureError::class)
                    override fun getHeaders(): Map<String, String> {
                        return params
                    }

                    override fun getBody(): ByteArray {
                        return body
                    }
                }
                addToRequestQueue(request)
            }
        }
    }

    fun createRequestAndAddToQueue(
        url: String,
        type: RequestType,
        method: Int?,
        successListener: Response.Listener<Any>,
        errorListener: Response.ErrorListener,
        imageOptions: ImageOptions?
    ) {
        when (type) {
            RequestType.STRING -> {
                val request = StringRequest(
                    method!!,
                    url,
                    successListener as Response.Listener<String>,
                    errorListener
                )
                addToRequestQueue(request)
            }
            RequestType.IMAGE -> {
                val request = ImageRequest(
                    url,
                    successListener as Response.Listener<Bitmap>,
                    imageOptions!!.maxWidth,
                    imageOptions.maxHeigth,
                    imageOptions.scaleType,
                    imageOptions.config,
                    errorListener
                )
                addToRequestQueue(request)
            }
        }
    }

    fun <T> addToRequestQueue(req: Request<T>) {
        requestQueue.add(req)
    }
}

enum class RequestType { STRING, IMAGE }
data class ImageOptions constructor(
    val maxWidth: Int,
    val maxHeigth: Int,
    val scaleType: ImageView.ScaleType,
    val config: Bitmap.Config
)