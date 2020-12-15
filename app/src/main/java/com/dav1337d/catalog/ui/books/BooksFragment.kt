package com.dav1337d.catalog.ui.books

import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dav1337d.catalog.R
import kotlinx.android.synthetic.main.books_fragment.*
import kotlinx.android.synthetic.main.tv_fragment.fab
import org.koin.android.viewmodel.ext.android.viewModel

class BooksFragment : Fragment() {

    private lateinit var booksAdapter: CustomBooksAdapter
    private lateinit var recyclerView: RecyclerView
    private val viewModel by viewModel<BooksViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        val rootView =  inflater.inflate(R.layout.books_fragment, container, false)
        recyclerView = rootView.findViewById(R.id.roomItemList)
        recyclerView.layoutManager = LinearLayoutManager(activity)

        booksAdapter = CustomBooksAdapter(requireContext())

        recyclerView.adapter = booksAdapter

        booksAdapter.onItemLongClick = { item ->
            val alertDialog: AlertDialog? = activity?.let {
                val builder = AlertDialog.Builder(it)
                builder.apply {
                    setPositiveButton("Delete",
                        DialogInterface.OnClickListener { dialog, id ->
                            viewModel.delete(item)
                        })
                    setNegativeButton("Cancel",
                        DialogInterface.OnClickListener { dialog, id ->
                            // User cancelled the dialog
                        })
                    setTitle("Delete this item from catalog?")
                }
                builder.create()
            }
            alertDialog?.show()
            alertDialog?.window?.setLayout(600,400)
        }
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        fab.setOnClickListener {
            findNavController().navigate(R.id.action_book_home_to_search)
        }

        viewModel.liveData.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                if (!it.isNullOrEmpty()) {
                    empty_text_view.visibility = View.GONE
                } else {
                    empty_text_view.visibility = View.VISIBLE
                }
                booksAdapter.setItems(it)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.catalog_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.login -> {
                findNavController().navigate(R.id.action_books_to_googleLogin)
                true
            }
            R.id.name -> viewModel.sortItemsBy("name")
            R.id.rating -> viewModel.sortItemsBy("personalRating")
            R.id.watched -> viewModel.sortItemsBy("readDate")
            R.id.release -> viewModel.sortItemsBy("first_air_date")
            else -> super.onOptionsItemSelected(item)
        }
    }

}
