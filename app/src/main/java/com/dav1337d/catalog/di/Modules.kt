package com.dav1337d.catalog.di

import com.dav1337d.catalog.model.tv.TVRepository
import com.dav1337d.catalog.ui.tv.SearchViewModel
import com.dav1337d.catalog.ui.tv.TVViewModel
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val applicationModule = module(override = true) {
   single { TVRepository() }
   viewModel { SearchViewModel(get()) }
   viewModel { TVViewModel(get()) }
}