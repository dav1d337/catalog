package com.dav1337d.catalog.ui.tv

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.volley.Response
import com.dav1337d.catalog.model.tv.EitherMovieOrSeries
import com.dav1337d.catalog.model.tv.TVRepository
import com.dav1337d.catalog.ui.base.BaseSearchViewModel
import kotlinx.coroutines.*
import org.koin.standalone.KoinComponent

class TVSearchViewModel constructor(private val tvRepository: TVRepository) :
    BaseSearchViewModel() {


    val results: MutableLiveData<List<EitherMovieOrSeries>> by lazy {
        MutableLiveData<List<EitherMovieOrSeries>>()
    }

    private val listener = Response.Listener<String> {
//        results.value = tvRepository.handleResponse(it).filter {
//            it.poster_path != "null"
//        }

        results.value = tvRepository.handleResponse(it)
            .filter { it.poster_path != "null" }
            .map {
                val listenerImages = Response.Listener<Bitmap> { img ->
                    it.poster = img
                }
                tvRepository.getPosterImage(id = it.poster_path, listener = listenerImages)
                it
            }


        results.value!!.forEachIndexed { index, eitherMovieOrSeries ->
            val listenerImages = Response.Listener<Bitmap> { img ->
                if (index < results.value!!.size) {
                    results.value?.get(index).let { tv ->
                        tv?.let {
                            it.poster = img
                            results.postValue(results.value)
                        }
                    }
                }
            }
            // TODO this in repo
            tvRepository.getPosterImage(
                id = eitherMovieOrSeries.poster_path,
                listener = listenerImages
            )
        }
    }

    fun insert(item: EitherMovieOrSeries, rating: Int, watchDate: String, comment: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                tvRepository.insert(item, rating, watchDate, comment)
            }
        }
    }

    fun search(query: String?) {
        viewModelScope.launch {
            queryTextChangedJob?.cancel()

            queryTextChangedJob = launch(Dispatchers.IO) {
                delay(250)
                tvRepository.searchTMB(query ?: "test", listener)
            }

        }
    }
}
