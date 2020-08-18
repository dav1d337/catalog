package kinf.koch.catalog.ui.tv

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kinf.koch.catalog.R
import kinf.koch.catalog.db.RoomSeriesMovie
import kinf.koch.catalog.model.tv.EitherMovieOrSeries
import kinf.koch.catalog.model.tv.TypeOfWatchable
import kinf.koch.catalog.ui.CustomAdapter
import kotlinx.android.synthetic.main.tv_fragment.*
import org.koin.android.viewmodel.ext.android.viewModel


class TVFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CustomAdapter
    val viewModel by viewModel<TVViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val rootView = inflater.inflate(R.layout.tv_fragment, container, false)
        recyclerView = rootView.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(activity)

        adapter = CustomAdapter(requireContext())

        recyclerView.adapter = adapter

        adapter.onItemLongClick = { item ->
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
            findNavController().navigate(R.id.action_tv_home_to_search)
        }

        viewModel.liveData.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                Log.i("hallo", "changed ${it.size}")
                if (!it.isNullOrEmpty()) {
                    textView.visibility = View.GONE
                } else {
                    textView.visibility = View.VISIBLE
                }
                adapter.setItems(it)
                /*
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


                 */
            }
        })
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.catalog_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.name -> viewModel.sortItemsBy("name")
            R.id.rating -> viewModel.sortItemsBy("rating")
            R.id.watched -> viewModel.sortItemsBy("watched")
            R.id.release -> viewModel.sortItemsBy("release")

            else -> super.onOptionsItemSelected(item)
        }
    }
}
