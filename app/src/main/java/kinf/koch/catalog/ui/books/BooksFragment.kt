package kinf.koch.catalog.ui.books

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kinf.koch.catalog.R

class BooksFragment : Fragment() {

    private lateinit var viewModel: BooksViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.books_fragment, container, false)
    }


}
