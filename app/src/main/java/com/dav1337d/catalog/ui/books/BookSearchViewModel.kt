package com.dav1337d.catalog.ui.books

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dav1337d.catalog.model.books.BookItem
import com.dav1337d.catalog.model.books.BookRepository
import kotlinx.coroutines.*
import org.koin.standalone.KoinComponent

class BookSearchViewModel constructor(private val bookRepository: BookRepository): ViewModel(),
    KoinComponent {

    var queryTextChangedJob: Job? = null

    val results: MutableLiveData<List<BookItem>> by lazy {
        MutableLiveData<List<BookItem>>()
    }

    fun insert(item: BookItem) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
           //     bookRepository.insert(item, rating, watchDate, comment)
            }
        }
    }

    fun search(query: String?) {
        viewModelScope.launch {
            queryTextChangedJob?.cancel()

            queryTextChangedJob = launch(Dispatchers.IO) {
                delay(50)

                bookRepository.searchBook("test")
            }
        }
    }
}
