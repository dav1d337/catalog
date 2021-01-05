package com.dav1337d.catalog.ui.games.detail

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dav1337d.catalog.model.games.GameDetailsResponse
import com.dav1337d.catalog.model.games.GamesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GameDetailViewModel constructor(private val gamesRepository: GamesRepository): ViewModel()  {

    val game: MediatorLiveData<GameDetailsResponse> = MediatorLiveData()
    val loading = MediatorLiveData<Boolean>()

    init {
        game.addSource(gamesRepository.gameDetailResult) {
            if (it != null) {
                game.value = it
            }
        }

        loading.addSource(gamesRepository.loading) {
            loading.value = it
        }
    }

    fun setGame(id: Long) {
        gamesRepository.getGameDetails(id)
    }

    fun insert(item: GameDetailsResponse, rating: Long, watchDate: String, comment: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                gamesRepository.insert(item, rating, watchDate, comment)
            }
        }
    }
}