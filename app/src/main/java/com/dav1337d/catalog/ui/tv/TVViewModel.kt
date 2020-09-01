package com.dav1337d.catalog.ui.tv

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dav1337d.catalog.db.RoomSeriesMovie
import com.dav1337d.catalog.model.tv.TVRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class TVViewModel constructor(private val tvRepository: TVRepository) : ViewModel() {

    val liveData = MediatorLiveData<List<RoomSeriesMovie>>()

    init {
        liveData.addSource(tvRepository.allSeriesMovie) {
            if (it != null) {
                liveData.value = it
            }
        }
    }

    fun delete(roomSeriesMovie: RoomSeriesMovie) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                tvRepository.delete(roomSeriesMovie)
            }

        }
    }


    fun sortItemsBy(by: String): Boolean {
        liveData.value?.let { seriesMovies ->
            when (by) {
                "name" -> {
                    if (seriesMovies != seriesMovies.sortedBy { it.name }) {
                        liveData.value = seriesMovies.sortedBy { it.name }
                    } else {
                        liveData.value = seriesMovies.sortedByDescending { it.name }
                    }
                }
                "first_air_date" -> {
                    if (seriesMovies != seriesMovies.sortedBy { it.first_air_date }) {
                        liveData.value = seriesMovies.sortedBy { it.first_air_date }
                    } else {
                        liveData.value = seriesMovies.sortedByDescending { it.first_air_date }
                    }
                }
                "watchDate" -> {
                    if (seriesMovies != seriesMovies.sortedBy { it.watchDate }) {
                        liveData.value = seriesMovies.sortedBy { it.watchDate }
                    } else {
                        liveData.value = seriesMovies.sortedByDescending { it.watchDate }
                    }
                }
                "personalRating" -> {
                    if (seriesMovies != seriesMovies.sortedBy { it.personalRating }) {
                        liveData.value = seriesMovies.sortedBy { it.personalRating }
                    } else {
                        liveData.value = seriesMovies.sortedByDescending { it.personalRating }
                    }
                }
            }
        }

        return true
    }
}
