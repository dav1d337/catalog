package com.dav1337d.catalog.ui.books

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.volley.Response
import com.dav1337d.catalog.model.books.BookItem
import com.dav1337d.catalog.model.books.BookRepository
import com.dav1337d.catalog.model.books.BookSearchResponse
import com.dav1337d.catalog.ui.base.BaseSearchViewModel
import com.google.gson.Gson
import kotlinx.coroutines.*

class BookSearchViewModel constructor(private val bookRepository: BookRepository): BaseSearchViewModel() {

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

                val listener = Response.Listener<String> {
                    val gson = Gson()
                    val result = gson.fromJson(it, BookSearchResponse::class.java)
                    results.value = result.items.filter { it.volumeInfo.publishedDate != null &&  it.volumeInfo.imageLinks != null &&  it.volumeInfo.authors != null}
                }

                query?.let {
                    bookRepository.searchBook(it, listener)
                }
            }
        }
    }
}
