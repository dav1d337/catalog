package com.dav1337d.catalog.ui.games

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.dav1337d.catalog.R
import com.dav1337d.catalog.ui.account.LoginActivity
import com.dav1337d.catalog.ui.base.BaseListFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.games_fragment.*
import org.koin.android.viewmodel.ext.android.viewModel

class GamesFragment :
    BaseListFragment<CustomGamesAdapter.ViewHolder, CustomGamesAdapter>(R.layout.games_fragment) {

    private val viewModel by viewModel<GamesViewModel>()

    override fun createAdapter(context: Context?): CustomGamesAdapter {
        return CustomGamesAdapter(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpDeleteDialog()

        viewModel.getGamesFromFirestore().observe(viewLifecycleOwner, Observer {
            if (it != null) {
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
                    setPositiveButton(
                        "Delete"
                    ) { _, id ->
                        viewModel.delete(item)
                    }
                    setNegativeButton(
                        "Cancel"
                    ) { _, id ->
                        // User cancelled the dialog
                    }
                    setTitle(getString(R.string.delete_confirmation))
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.catalog_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                Firebase.auth.signOut()
                val intent = Intent(activity, LoginActivity::class.java)
                startActivity(intent)
                activity?.finish()
                return true
            }
            R.id.name -> viewModel.sortItemsBy("name")
            R.id.rating -> viewModel.sortItemsBy("personalRating")
            R.id.watched -> viewModel.sortItemsBy("watchDate")
            R.id.release -> viewModel.sortItemsBy("first_air_date")
            else -> super.onOptionsItemSelected(item)
        }
    }

}
