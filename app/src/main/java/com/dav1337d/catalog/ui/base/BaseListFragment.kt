package com.dav1337d.catalog.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dav1337d.catalog.R
import com.dav1337d.catalog.ui.books.BooksViewModel

abstract class BaseListFragment(layoutId: Int) : Fragment() {

    private lateinit var viewModel: ViewModel
    private lateinit var recyclerView: RecyclerView
    // todo use bundle for laout?
    private  var layout: Int = layoutId
    private lateinit var adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(layout, container, false)
        recyclerView = rootView.findViewById(R.id.roomItemList)
        recyclerView.layoutManager = LinearLayoutManager(activity)


        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(BooksViewModel::class.java)
        // TODO: Use the ViewModel
    }
}