package com.dav1337d.catalog.ui.base

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dav1337d.catalog.R
import com.dav1337d.catalog.ui.books.BooksViewModel
import com.google.android.material.snackbar.Snackbar

abstract class BaseListFragment<VH : RecyclerView.ViewHolder, out T : RecyclerView.Adapter<VH>>(
    @LayoutRes private val layoutId: Int
) : Fragment() {

    private lateinit var recyclerView: RecyclerView
    lateinit var adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>

    protected abstract fun createAdapter(context: Context?): T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(layoutId, container, false)
        recyclerView = rootView.findViewById(R.id.roomItemList)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        val adapter = createAdapter(context)
        this.adapter = adapter as RecyclerView.Adapter<RecyclerView.ViewHolder>
        recyclerView.adapter = adapter
        return rootView
    }
}