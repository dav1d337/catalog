package kinf.koch.catalog.ui.tv

import android.os.Bundle
import android.provider.Contacts
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kinf.koch.catalog.R
import kinf.koch.catalog.model.tv.EitherMovieOrSeries
import kotlinx.android.synthetic.main.search_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import org.koin.android.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SearchResultsAdapter
    private lateinit var clickListener: OnClickListener
    private lateinit var clickListenerSave: OnClickListener
    private lateinit var searchView: SearchView

    val viewModel by viewModel<SearchViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView = inflater.inflate(R.layout.search_fragment, container, false)
        searchView = rootView.findViewById(R.id.searchView)
        searchView.queryHint = "Enter title..."
        recyclerView = rootView.findViewById(R.id.recyclerView)
        clickListenerSave = object : OnClickListener {
            override fun onSaveClick(item: EitherMovieOrSeries, rating: Int, watchDate:String, comment: String) {
                viewModel.insert(item, rating, watchDate, comment)
            }
        }
        clickListener = object : OnClickListener {
            override fun onCheckBoxClick(item: EitherMovieOrSeries) {
                val dialog = AddToDbDialogFragment(clickListenerSave, item)
                dialog.show(fragmentManager!!, "dialog")
            }
        }
        recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = SearchResultsAdapter(listOf(), clickListener)
        recyclerView.adapter = adapter
        return rootView
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.results.observe(viewLifecycleOwner, Observer {
            adapter.setItems(it)
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