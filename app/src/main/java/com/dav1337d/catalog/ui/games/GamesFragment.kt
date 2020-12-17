package com.dav1337d.catalog.ui.games

import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.dav1337d.catalog.R
import com.dav1337d.catalog.ui.base.BaseListFragment
import com.dav1337d.catalog.ui.books.BooksViewModel
import com.dav1337d.catalog.ui.books.CustomBooksAdapter
import kotlinx.android.synthetic.main.games_fragment.*
import org.koin.android.viewmodel.ext.android.viewModel

class GamesFragment : BaseListFragment<CustomGamesAdapter.ViewHolder, CustomGamesAdapter>(R.layout.games_fragment) {

    private val viewModel by viewModel<GamesViewModel>()

    override fun createAdapter(context: Context?): CustomGamesAdapter {
        return CustomGamesAdapter(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.games_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        fab.setOnClickListener {
            findNavController().navigate(R.id.action_games_home_to_search)
        }
    }

}
