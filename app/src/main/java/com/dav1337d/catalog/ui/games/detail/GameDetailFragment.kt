package com.dav1337d.catalog.ui.games.detail

import android.R.attr.data
import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.dav1337d.catalog.App
import com.dav1337d.catalog.R
import com.dav1337d.catalog.databinding.GameDetailBinding
import com.dav1337d.catalog.util.ImageSaver
import kotlinx.android.synthetic.main.game_detail.*
import org.koin.android.viewmodel.ext.android.viewModel


class GameDetailFragment: Fragment() {

    private val args: GameDetailFragmentArgs by navArgs()
    private val viewModel by viewModel<GameDetailViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: GameDetailBinding = DataBindingUtil.inflate(
            inflater, R.layout.game_detail, container, false
        )
        val view: View = binding.root
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

      //  (activity as AppCompatActivity).setSupportActionBar(toolbar)
        viewModel.game.observe(viewLifecycleOwner, Observer {
            (activity as AppCompatActivity).supportActionBar?.title = "blubb"
        })

        val id = args.gameId
        viewModel.setGame(id)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loading.observe(viewLifecycleOwner, Observer {
            if (it) {
                progress_loader.visibility = View.VISIBLE
            } else {
                progress_loader.visibility = View.GONE
                val fileName = (viewModel.game.value?.name + "backdrop" + ".png").replace("/", "")
                backdrop.setImageBitmap(ImageSaver(App.appContext!!).setFileName(fileName)
                    .setDirectoryName("images").load())
            }
        })
    }
}