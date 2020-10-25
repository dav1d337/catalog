package com.dav1337d.catalog.ui.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dav1337d.catalog.model.books.BookItem
import kotlinx.coroutines.Job
import org.koin.standalone.KoinComponent

abstract class BaseSearchViewModel: ViewModel(),
    KoinComponent {

    var queryTextChangedJob: Job? = null

//    val results: MutableLiveData<List<Any>> by lazy {
//        MutableLiveData<List<Any>>()
//    }
}