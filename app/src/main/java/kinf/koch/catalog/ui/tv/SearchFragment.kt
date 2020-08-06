package kinf.koch.catalog.ui.tv

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kinf.koch.catalog.R
import kinf.koch.catalog.model.tv.EitherMovieOrSeries
import kotlinx.android.synthetic.main.search_fragment.*

class SearchFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SearchResultsAdapter
    private lateinit var clickListener: OnClickListener

    //  private lateinit var dataset: Array<String>
    private lateinit var viewModel: SearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //     initDataset()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView = inflater.inflate(R.layout.search_fragment, container, false)
        recyclerView = rootView.findViewById(R.id.recyclerView)
        clickListener = object : OnClickListener {
            override fun onCheckBoxClick(item: EitherMovieOrSeries) {
              //  findNavController().navigate(R.id.action_searchTV_to_addToDbDialogFragment)
                val dialog = AddToDbDialogFragment()
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
        viewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)

        viewModel.results.observe(viewLifecycleOwner, Observer {
            //recyclerView.adapter = SearchResultsAdapter(it, clickListener)
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