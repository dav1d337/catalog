package com.dav1337d.catalog.ui.games

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
import com.dav1337d.catalog.model.games.Game
import com.dav1337d.catalog.model.tv.EitherMovieOrSeries
import com.dav1337d.catalog.ui.base.AddToDbDialogFragment
import com.dav1337d.catalog.ui.base.OnClickListener
import org.koin.android.viewmodel.ext.android.viewModel


class GamesSearchFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var gamesAdapter: SearchResultsGamesAdapter
    private lateinit var clickListener: OnClickListener<Game>
    private lateinit var clickListenerSave: OnClickListener<Game>
    private lateinit var searchView: SearchView

    val viewModel by viewModel<GamesSearchViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView = inflater.inflate(R.layout.search_fragment, container, false)
        searchView = rootView.findViewById(R.id.searchView)
        searchView.queryHint = "Enter title..."

        recyclerView = rootView.findViewById(R.id.roomItemList)
        clickListenerSave = object : OnClickListener<Game> {
            override fun onSaveClick(
                item: Game,
                rating: Int,
                watchDate: String,
                comment: String
            ) {
                viewModel.insert(item, rating, watchDate, comment)
                searchView.setQuery(searchView.query, true) // to update the watched mark
            }
        }
        clickListener = object : OnClickListener<Game> {
            override fun onCheckBoxClick(item: Game) {
                val dialog = AddToDbDialogFragment(clickListenerSave as OnClickListener<Any>, item)
                dialog.show(fragmentManager!!, "dialog")
            }
        }
        recyclerView.layoutManager = LinearLayoutManager(activity)
        gamesAdapter = SearchResultsGamesAdapter(listOf(), clickListener)
        recyclerView.adapter = gamesAdapter
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.liveData.observe(viewLifecycleOwner, Observer {
            if (it.isNullOrEmpty()) {
                gamesAdapter.setItems(listOf())
            }
            gamesAdapter.setItems(it)
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
                        gamesAdapter.setItems(listOf())
                    }
                }
                return true
            }
        })
    }
}