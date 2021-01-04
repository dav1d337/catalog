package com.dav1337d.catalog.ui.books

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.volley.Response
import com.dav1337d.catalog.model.books.BookItem
import com.dav1337d.catalog.model.books.BookRepository
import com.dav1337d.catalog.model.books.BookSearchResponse
import com.dav1337d.catalog.ui.base.BaseSearchViewModel
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BookSearchViewModel constructor(private val bookRepository: BookRepository) :
    BaseSearchViewModel<BookItem>() {

    val loading: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)

    fun insert(
        item: BookItem, rating: Long, readDate: String, comment: String// , status: BookStatus
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                bookRepository.insert(item, rating, readDate, comment) // , status
            }
        }
    }

    private suspend fun heavyWork(query: String?) = withContext(Dispatchers.Default) {
        val listener = Response.Listener<String> {
            val gson = Gson()
            val result = gson.fromJson(it, BookSearchResponse::class.java)

            results.value =
                result.items.filter { it.volumeInfo.publishedDate != null && it.volumeInfo.imageLinks != null && it.volumeInfo.authors != null }
                    .map {
                        val listenerImage = Response.Listener<Bitmap> { img ->
                            it.thumbnail = img
                            results.postValue(results.value)
                            loading.postValue(false)
                        }
                        bookRepository.getBookImage(it, listenerImage)
                        if (bookRepository.contains(it.id)) {
                            it.read = true
                        }
                        it
                    }
        }

        query?.let {
            bookRepository.searchBook(it, listener)
        }

    }

    fun search(query: String?) {
        if (query.isNullOrEmpty()) {
            loading.postValue(false)
        } else {
            loading.postValue(true)
        }
        viewModelScope.launch {
            queryTextChangedJob?.cancel()

            queryTextChangedJob = launch(Dispatchers.IO) {
                delay(250)
                heavyWork(query)
            }
        }
    }
}
