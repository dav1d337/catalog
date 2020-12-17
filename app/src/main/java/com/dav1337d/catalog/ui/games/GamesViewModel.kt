package com.dav1337d.catalog.ui.games

import androidx.lifecycle.ViewModel
import com.dav1337d.catalog.model.books.BookRepository
import com.dav1337d.catalog.model.games.GamesRepository

class GamesViewModel constructor(val gamesRepository: GamesRepository): ViewModel() {

    init {
        gamesRepository.getAccessToken()
    }
}
