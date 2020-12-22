package com.dav1337d.catalog.ui.games

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.dav1337d.catalog.R
import com.dav1337d.catalog.ui.base.BaseListFragment
import kotlinx.android.synthetic.main.games_fragment.*
import org.koin.android.viewmodel.ext.android.viewModel

class GamesFragment : BaseListFragment<CustomGamesAdapter.ViewHolder, CustomGamesAdapter>(R.layout.games_fragment) {

    private val viewModel by viewModel<GamesViewModel>()

    override fun createAdapter(context: Context?): CustomGamesAdapter {
        return CustomGamesAdapter(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpDeleteDialog()

        viewModel.liveData.observe(viewLifecycleOwner, Observer {
            if (it != null) {

                Log.i("hallo GamesFragment", it.size.toString())
                if (!it.isNullOrEmpty()) {
                    empty_text_view.visibility = View.GONE
                } else {
                    empty_text_view.visibility = View.VISIBLE
                }
                (adapter as CustomGamesAdapter).setItems(it)
            }
        })
    }

    private fun setUpDeleteDialog() {
        (adapter as CustomGamesAdapter).onItemLongClick = { item ->
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
            alertDialog?.window?.setLayout(600, 400)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        fab.setOnClickListener {
            findNavController().navigate(R.id.action_games_home_to_search)
        }
    }

}
