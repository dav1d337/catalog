package kinf.koch.catalog.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import kinf.koch.catalog.ui.books.BooksViewModel

class BaseListFragment() : Fragment() {

    private lateinit var viewModel: BooksViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(BooksViewModel::class.java)
        // TODO: Use the ViewModel
    }
}