package kinf.koch.catalog.ui.tv

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kinf.koch.catalog.R
import kinf.koch.catalog.model.tv.EitherMovieOrSeries
import kinf.koch.catalog.model.tv.TypeOfWatchable
import kinf.koch.catalog.ui.CustomAdapter
import kotlinx.android.synthetic.main.tv_fragment.*
import org.koin.android.viewmodel.ext.android.viewModel

class TVFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var dataset: Array<String>
    private lateinit var adapter: CustomAdapter
    //private lateinit var viewModel: TVViewModel
    val viewModel by viewModel<TVViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDataset()
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val rootView = inflater.inflate(R.layout.tv_fragment, container, false)
        recyclerView = rootView.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = CustomAdapter(requireContext())
        recyclerView.adapter = adapter

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
     //   viewModel = ViewModelProviders.of(this).get(TVViewModel::class.java)

        fab.setOnClickListener {
            findNavController().navigate(R.id.action_tv_home_to_search)
        }

        viewModel.allSeries.observe(viewLifecycleOwner, Observer {
            adapter.setItems(it.map {
                EitherMovieOrSeries(
                    TypeOfWatchable.SERIES,
                    it.original_name,
                    it.name,
                    listOf(),
                    it.first_air_date,
                    it.overview,
                    it.rating_tmdb,
                    it.id_tmdb,
                    it.backdrop_path,
                    it.poster_path
                )
            })
        })

    }

    /**
     * Generates Strings for RecyclerView's adapter. This data would usually come
     * from a local content provider or remote server.
     */
    private fun initDataset() {
        dataset = Array(30, {i -> "This is element # $i"})
    }
}
