package com.dav1337d.catalog.ui.games

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.volley.Response
import com.dav1337d.catalog.model.games.GamesRepository
import com.dav1337d.catalog.model.tv.EitherMovieOrSeries
import com.dav1337d.catalog.model.tv.TVRepository
import com.dav1337d.catalog.ui.base.BaseSearchViewModel
import kotlinx.coroutines.*
import org.koin.standalone.KoinComponent

class GamesSearchViewModel constructor(private val gamesRepository: GamesRepository) :
    BaseSearchViewModel<EitherMovieOrSeries>() {


    fun insert(item: EitherMovieOrSeries, rating: Int, watchDate: String, comment: String) {
//        viewModelScope.launch {
//            withContext(Dispatchers.IO) {
//                tvRepository.insert(item, rating, watchDate, comment)
//            }
//        }
    }

    init {
        gamesRepository.getAccessToken()
    }

    fun search(query: String?) {
        viewModelScope.launch {
            queryTextChangedJob?.cancel()

            queryTextChangedJob = launch(Dispatchers.IO) {
                delay(250)
                gamesRepository.searchGames(query ?: "test")
            }

        }
    }
}
