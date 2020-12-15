package com.dav1337d.catalog.ui.books

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dav1337d.catalog.db.RoomBook
import com.dav1337d.catalog.model.books.BookRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BooksViewModel constructor(val bookRepository: BookRepository) : ViewModel() {

    val liveData = MediatorLiveData<List<RoomBook>>()

    init {
        // val account = GoogleSignIn.getLastSignedInAccount(App.appContext)
        // Log.i("hallo account", account?.email.toString())

        liveData.addSource(bookRepository.allBooks) {
            if (it != null) {
                liveData.value = it
            }
        }

    }

    fun delete(roomBook: RoomBook) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                bookRepository.delete(roomBook)
            }
        }
    }

    fun sortItemsBy(by: String): Boolean {
        liveData.value?.let { seriesMovies ->
            when (by) {
                "name" -> {
                    if (seriesMovies != seriesMovies.sortedBy { it.title }) {
                        liveData.value = seriesMovies.sortedBy { it.title }
                    } else {
                        liveData.value = seriesMovies.sortedByDescending { it.title }
                    }
                }
                "first_air_date" -> {
                    if (seriesMovies != seriesMovies.sortedBy { it.year }) {
                        liveData.value = seriesMovies.sortedBy { it.year }
                    } else {
                        liveData.value = seriesMovies.sortedByDescending { it.year }
                    }
                }
                "readDate" -> {
                    if (seriesMovies != seriesMovies.sortedBy { it.readDate }) {
                        liveData.value = seriesMovies.sortedBy { it.readDate }
                    } else {
                        liveData.value = seriesMovies.sortedByDescending { it.readDate }
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
