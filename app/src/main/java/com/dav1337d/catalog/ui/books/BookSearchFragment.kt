package com.dav1337d.catalog.ui.books

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.dav1337d.catalog.R
import org.koin.android.viewmodel.ext.android.viewModel

class BookSearchFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var booksAdapter: SearchResultsBooksAdapter
    private lateinit var clickListener: OnClickListener
    private lateinit var clickListenerSave: OnClickListener
    private lateinit var searchView: SearchView

    val viewModel by viewModel<BookSearchViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView = inflater.inflate(R.layout.search_fragment, container, false)
        searchView = rootView.findViewById(R.id.searchView)
//        searchView.queryHint = "Enter title..."
//        recyclerView = rootView.findViewById(R.id.recyclerView)
//        clickListenerSave = object : OnClickListener {
//            override fun onSaveClick(item: Book) {
//                viewModel.insert(item)
//            }
//        }
//        clickListener = object : OnClickListener {
//            override fun onCheckBoxClick(item: EitherMovieOrSeries) {
//                val dialog = AddToDbDialogFragment(clickListenerSave, item)
//                dialog.show(fragmentManager!!, "dialog")
//            }
//        }
//        recyclerView.layoutManager = LinearLayoutManager(activity)
//        adapter = SearchResultsAdapter(listOf(), clickListener)
//        recyclerView.adapter = adapter
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
                viewModel.search(newText)
                return true
            }
        })
    }
}