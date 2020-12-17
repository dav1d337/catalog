package com.dav1337d.catalog.ui.books

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dav1337d.catalog.R
import com.dav1337d.catalog.model.books.BookItem
import com.dav1337d.catalog.ui.base.AddToDbDialogFragment
import com.dav1337d.catalog.ui.base.OnClickListener
import org.koin.android.viewmodel.ext.android.viewModel

class BookSearchFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var booksAdapter: SearchResultsBooksAdapter
    private lateinit var clickListener: OnClickListener<BookItem>
    private lateinit var clickListenerSave: OnClickListener<BookItem>
    private lateinit var searchView: SearchView

    val viewModel by viewModel<BookSearchViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView = inflater.inflate(R.layout.search_fragment, container, false)
        searchView = rootView.findViewById(R.id.searchView)
        searchView.queryHint = "Enter title..."
        recyclerView = rootView.findViewById(R.id.roomItemList)
        clickListenerSave = object : OnClickListener<BookItem> {
            override fun onSaveClick(
                item: BookItem,
                rating: Int,
                readDate: String,
                comment: String
            ) {
                viewModel.insert(item, rating, readDate, comment)
                searchView.setQuery(searchView.query, true) // to update the watched mark
            }
        }
        clickListener = object : OnClickListener<BookItem> {
            override fun onCheckBoxClick(item: BookItem) {
                val dialog = AddToDbDialogFragment(clickListenerSave as OnClickListener<Any>, item)
                dialog.show(fragmentManager!!, "dialog")
            }
        }
        recyclerView.layoutManager = LinearLayoutManager(activity)
        booksAdapter = SearchResultsBooksAdapter(listOf(), clickListener)
        recyclerView.adapter = booksAdapter
        return rootView
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.results.observe(viewLifecycleOwner, Observer {
            if (it.isNullOrEmpty()) {
                booksAdapter.setItems(listOf())
            }
            booksAdapter.setItems(it)
        })

        searchView.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.search(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    viewModel.search(it)
                    if (it.isBlank()) {
                        booksAdapter.setItems(listOf())
                    }
                }
                return true
            }
        })
    }
}