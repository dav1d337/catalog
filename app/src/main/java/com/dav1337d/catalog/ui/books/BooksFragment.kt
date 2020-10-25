package com.dav1337d.catalog.ui.books

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.dav1337d.catalog.R
import kotlinx.android.synthetic.main.tv_fragment.*
import org.koin.android.viewmodel.ext.android.viewModel

class BooksFragment : Fragment() {

    private val viewModel by viewModel<BooksViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val rootView =  inflater.inflate(R.layout.books_fragment, container, false)

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        fab.setOnClickListener {
            findNavController().navigate(R.id.action_book_home_to_search)
        }

        viewModel.sayHello()
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
            else -> super.onOptionsItemSelected(item)
        }
    }

}
