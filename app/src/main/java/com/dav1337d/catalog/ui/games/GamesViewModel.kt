package com.dav1337d.catalog.ui.games

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dav1337d.catalog.db.RoomGame
import com.dav1337d.catalog.model.games.GamesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GamesViewModel constructor(val gamesRepository: GamesRepository): ViewModel() {

    val liveData = MediatorLiveData<List<RoomGame>>()

    init {
        liveData.addSource(gamesRepository.allRoomGames) {
            if (it != null) {
                liveData.value = it
            }
        }
    }

    fun delete(roomGame: RoomGame) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                gamesRepository.delete(roomGame)
            }
        }
    }

    fun sortItemsBy(by: String): Boolean {
        liveData.value?.let { game ->
            when (by) {
                "name" -> {
                    if (game != game.sortedBy { it.name }) {
                        liveData.value = game.sortedBy { it.name }
                    } else {
                        liveData.value = game.sortedByDescending { it.name }
                    }
                }
                "first_air_date" -> {
                    if (game != game.sortedBy { it.first_release_date }) {
                        liveData.value = game.sortedBy { it.first_release_date }
                    } else {
                        liveData.value = game.sortedByDescending { it.first_release_date }
                    }
                }
                "readDate" -> {
                    if (game != game.sortedBy { it.playDate }) {
                        liveData.value = game.sortedBy { it.playDate }
                    } else {
                        liveData.value = game.sortedByDescending { it.playDate }
                    }
                }
                "personalRating" -> {
                    if (game != game.sortedBy { it.personalRating }) {
                        liveData.value = game.sortedBy { it.personalRating }
                    } else {
                        liveData.value = game.sortedByDescending { it.personalRating }
                    }
                }
            }
        }

        return true
    }
}
