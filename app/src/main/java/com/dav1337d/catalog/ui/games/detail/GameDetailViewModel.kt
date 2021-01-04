package com.dav1337d.catalog.ui.games.detail

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dav1337d.catalog.model.games.GameDetailsResponse
import com.dav1337d.catalog.model.games.GamesRepository

class GameDetailViewModel constructor(private val gamesRepository: GamesRepository): ViewModel()  {

    val game = MediatorLiveData<GameDetailsResponse>()
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
}