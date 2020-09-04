package com.dav1337d.catalog.ui.tv

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.volley.Response
import com.dav1337d.catalog.model.tv.EitherMovieOrSeries
import com.dav1337d.catalog.model.tv.TVRepository
import kotlinx.coroutines.*
import org.koin.standalone.KoinComponent

class SearchViewModel constructor(private val tvRepository: TVRepository): ViewModel(),
    KoinComponent {

    var queryTextChangedJob: Job? = null

    val results: MutableLiveData<List<EitherMovieOrSeries>> by lazy {
        MutableLiveData<List<EitherMovieOrSeries>>()
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
                delay(50)
                val listener = Response.Listener<String> {
                    results.value = tvRepository.handleResponse(it).filter { it.poster_path != "null" }
                    results.value!!.forEachIndexed { index, eitherMovieOrSeries ->
                        val listenerImages = Response.Listener<Bitmap> {img ->
                            if (index < results.value!!.size) {
                                results.value?.get(index).let { tv ->
                                    tv?.let {
                                        runBlocking {
                                            it.watched = tvRepository.contains(it.original_name)
                                        }
                                        it.poster = img
                                        results.postValue(results.value)
                                    }
                                }
                            }
                        }
                        tvRepository.getPosterImage(id = eitherMovieOrSeries.poster_path, listener = listenerImages)
                    }
                }
                tvRepository.searchTMB(query?:"test", listener)
            }

        }
    }
}
