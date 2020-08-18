package kinf.koch.catalog.ui.tv

import android.R
import android.graphics.BitmapFactory
import android.widget.ImageView
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kinf.koch.catalog.db.RoomSeriesMovie
import kinf.koch.catalog.model.tv.TVRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException


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
                "release" -> {
                    if (seriesMovies != seriesMovies.sortedBy { it.first_air_date }) {
                        liveData.value = seriesMovies.sortedBy { it.first_air_date }
                    } else {
                        liveData.value = seriesMovies.sortedByDescending { it.first_air_date }
                    }
                }
            }
        }

        return true
    }
}
