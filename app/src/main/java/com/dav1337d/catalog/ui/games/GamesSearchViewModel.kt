package com.dav1337d.catalog.ui.games

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.viewModelScope
import com.dav1337d.catalog.model.games.Game
import com.dav1337d.catalog.model.games.GamesRepository
import com.dav1337d.catalog.ui.base.BaseSearchViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GamesSearchViewModel constructor(private val gamesRepository: GamesRepository) :
    BaseSearchViewModel<Game>() {


    val liveData = MediatorLiveData<List<Game>>()

    fun insert(item: Game, rating: Int, watchDate: String, comment: String) {
//        viewModelScope.launch {
//            withContext(Dispatchers.IO) {
//                tvRepository.insert(item, rating, watchDate, comment)
//            }
//        }
    }

    init {
        gamesRepository.getAccessToken()
        liveData.addSource(gamesRepository.searchResults) {
            if (it != null) {
                liveData.value = it
            }
        }
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
