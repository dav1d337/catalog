package com.dav1337d.catalog.ui.tv

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
import com.dav1337d.catalog.R
import kotlinx.android.synthetic.main.tv_fragment.*
import org.koin.android.viewmodel.ext.android.viewModel


class TVFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var tvAdapter: CustomTVAdapter
    private val viewModel by viewModel<TVViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        val rootView = inflater.inflate(R.layout.tv_fragment, container, false)
        recyclerView = rootView.findViewById(R.id.roomItemList)
        recyclerView.layoutManager = LinearLayoutManager(activity)

        tvAdapter = CustomTVAdapter(requireContext())

        recyclerView.adapter = tvAdapter

        tvAdapter.onItemLongClick = { item ->
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
                tvAdapter.setItems(it)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.catalog_menu, menu)
        menu.findItem(R.id.account).isVisible = false
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.name -> viewModel.sortItemsBy("name")
            R.id.rating -> viewModel.sortItemsBy("personalRating")
            R.id.watched -> viewModel.sortItemsBy("watchDate")
            R.id.release -> viewModel.sortItemsBy("first_air_date")
            else -> super.onOptionsItemSelected(item)
        }
    }
}
