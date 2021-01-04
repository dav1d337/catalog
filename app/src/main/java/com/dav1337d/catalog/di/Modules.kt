package com.dav1337d.catalog.di

import com.dav1337d.catalog.model.books.BookRepository
import com.dav1337d.catalog.model.games.GamesRepository
import com.dav1337d.catalog.model.tv.TVRepository
import com.dav1337d.catalog.ui.books.BookSearchViewModel
import com.dav1337d.catalog.ui.books.BooksViewModel
import com.dav1337d.catalog.ui.games.search.GamesSearchViewModel
import com.dav1337d.catalog.ui.games.GamesViewModel
import com.dav1337d.catalog.ui.games.detail.GameDetailViewModel
import com.dav1337d.catalog.ui.tv.TVSearchViewModel
import com.dav1337d.catalog.ui.tv.TVViewModel
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val applicationModule = module(override = true) {
    single { TVRepository() }
    viewModel { TVSearchViewModel(get()) }
    viewModel { TVViewModel(get()) }

    single { BookRepository() }
    viewModel { BookSearchViewModel(get()) }
    viewModel { BooksViewModel(get()) }

    single { GamesRepository() }
    viewModel { GamesSearchViewModel(get()) }
    viewModel { GamesViewModel(get()) }
    viewModel { GameDetailViewModel(get()) }
}