package com.dav1337d.catalog.ui.games

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dav1337d.catalog.R
import kotlinx.android.synthetic.main.games_fragment.*

class GamesFragment : Fragment() {

    private lateinit var viewModel: GamesViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.games_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(GamesViewModel::class.java)
        // TODO: Use the ViewModel

        fab.setOnClickListener {
            text.text = "You clicked!"
        }
    }

}
