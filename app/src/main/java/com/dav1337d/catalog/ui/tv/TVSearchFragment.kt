package com.dav1337d.catalog.ui.tv

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
import com.dav1337d.catalog.model.tv.EitherMovieOrSeries
import com.dav1337d.catalog.ui.base.AddToDbDialogFragment
import com.dav1337d.catalog.ui.base.OnClickListener
import kotlinx.android.synthetic.main.game_detail.*
import org.koin.android.viewmodel.ext.android.viewModel


class TVSearchFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var tvAdapter: SearchResultsTVAdapter
    private lateinit var clickListener: OnClickListener<EitherMovieOrSeries>
    private lateinit var clickListenerSave: OnClickListener<EitherMovieOrSeries>
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
        clickListenerSave = object : OnClickListener<EitherMovieOrSeries> {
            override fun onSaveClick(
                item: EitherMovieOrSeries,
                rating: Long,
                watchDate: String,
                comment: String
            ) {
                viewModel.insert(item, rating, watchDate, comment)
                searchView.setQuery(searchView.query, true) // to update the watched mark
            }
        }
        clickListener = object : OnClickListener<EitherMovieOrSeries> {
            override fun onCheckBoxClick(item: EitherMovieOrSeries) {
                val dialog = AddToDbDialogFragment(clickListenerSave as OnClickListener<Any>, item)
                dialog.show(fragmentManager!!, "dialog")
            }
        }
        recyclerView.layoutManager = LinearLayoutManager(activity)
        tvAdapter = SearchResultsTVAdapter(listOf(), clickListener)
        recyclerView.adapter = tvAdapter
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.results.observe(viewLifecycleOwner, Observer {
            if (it.isNullOrEmpty()) {
                tvAdapter.setItems(listOf())
            }
            tvAdapter.setItems(it)
        })

        viewModel.loading.observe(viewLifecycleOwner, Observer {
            if (it) {
                progress_loader.visibility = View.VISIBLE
            } else {
                progress_loader.visibility = View.GONE
            }
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
                        tvAdapter.setItems(listOf())
                    }
                }
                return true
            }
        })
    }
}