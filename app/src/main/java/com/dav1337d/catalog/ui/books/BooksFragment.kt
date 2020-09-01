package com.dav1337d.catalog.ui.books

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dav1337d.catalog.R

class BooksFragment : Fragment() {

    private lateinit var viewModel: BooksViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.books_fragment, container, false)
    }


}
