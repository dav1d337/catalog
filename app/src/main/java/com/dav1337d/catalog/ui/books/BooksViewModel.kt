package com.dav1337d.catalog.ui.books

import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModel
import com.dav1337d.catalog.model.books.BookRepository
import com.dav1337d.catalog.ui.App
import com.google.android.gms.auth.api.signin.GoogleSignIn

class BooksViewModel constructor(val bookRepository: BookRepository) : ViewModel() {
    // TODO: Implement the ViewModel

    init {
        val account = GoogleSignIn.getLastSignedInAccount(App.appContext)
        Log.i("hallo account", account?.email.toString())
    }

    fun sayHello() {
        Log.i("hallo", "hallo")
       // bookRepository.addBookToBookshelf()
    }
}
