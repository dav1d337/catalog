package kinf.koch.catalog.ui.tv

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kinf.koch.catalog.R
import kinf.koch.catalog.ui.CustomAdapter
import kotlinx.android.synthetic.main.tv_fragment.*

class TVFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var dataset: Array<String>
    private lateinit var viewModel: TVViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDataset()
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val rootView = inflater.inflate(R.layout.tv_fragment, container, false)
        recyclerView = rootView.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = CustomAdapter(dataset)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(TVViewModel::class.java)
        // TODO: Use the ViewModel

        fab.setOnClickListener {
            findNavController().navigate(R.id.action_tv_home_to_search)
        }
    }

    /**
     * Generates Strings for RecyclerView's adapter. This data would usually come
     * from a local content provider or remote server.
     */
    private fun initDataset() {
        dataset = Array(30, {i -> "This is element # $i"})
    }
}
