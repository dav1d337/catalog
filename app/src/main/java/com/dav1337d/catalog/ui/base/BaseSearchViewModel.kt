package com.dav1337d.catalog.ui.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Job
import org.koin.standalone.KoinComponent

abstract class BaseSearchViewModel<V> : ViewModel(),
    KoinComponent {

    var queryTextChangedJob: Job? = null

    val results: MutableLiveData<List<V>> by lazy {
        MutableLiveData<List<V>>()
    }
}