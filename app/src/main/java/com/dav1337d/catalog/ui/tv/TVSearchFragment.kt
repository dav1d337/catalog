package com.dav1337d.catalog.ui.tv

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dav1337d.catalog.R
import com.dav1337d.catalog.model.tv.EitherMovieOrSeries
import org.koin.android.viewmodel.ext.android.viewModel


class TVSearchFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var TVAdapter: SearchResultsTVAdapter
    private lateinit var clickListener: OnClickListener
    private lateinit var clickListenerSave: OnClickListener
    private lateinit var searchView: SearchView

    val viewModel by viewModel<TVSearchViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView = inflater.inflate(R.layout.search_fragment, container, false)
        searchView = rootView.findViewById(R.id.searchView)
        searchView.queryHint = "Enter title..."

        recyclerView = rootView.findViewById(R.id.roomItemList)
        clickListenerSave = object : OnClickListener {
            override fun onSaveClick(
                item: EitherMovieOrSeries,
                rating: Int,
                watchDate: String,
                comment: String
            ) {
                viewModel.insert(item, rating, watchDate, comment)
                searchView.setQuery(searchView.query, true) // to update the watched mark
            }
        }
        clickListener = object : OnClickListener {
            override fun onCheckBoxClick(item: EitherMovieOrSeries) {
                val dialog = AddToDbDialogFragment(clickListenerSave, item)
                dialog.show(fragmentManager!!, "dialog")
            }
        }
        recyclerView.layoutManager = LinearLayoutManager(activity)
        TVAdapter = SearchResultsTVAdapter(listOf(), clickListener)
        recyclerView.adapter = TVAdapter
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.results.observe(viewLifecycleOwner, Observer {
            if (it.isNullOrEmpty()) {
                TVAdapter.setItems(listOf())
            }
            TVAdapter.setItems(it)
        })

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.search(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    viewModel.search(it)
                    if (it.isBlank()) {
                        TVAdapter.setItems(listOf())
                    }
                }
                return true
            }
        })
    }
}