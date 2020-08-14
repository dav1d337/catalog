package kinf.koch.catalog.di

import kinf.koch.catalog.db.AppDatabase
import kinf.koch.catalog.db.RoomSeriesDao
import kinf.koch.catalog.model.tv.TVRepository
import kinf.koch.catalog.ui.tv.SearchViewModel
import kinf.koch.catalog.ui.tv.TVViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val applicationModule = module(override = true) {
   single { TVRepository() }
   viewModel { SearchViewModel(get()) }
   viewModel { TVViewModel(get()) }
}