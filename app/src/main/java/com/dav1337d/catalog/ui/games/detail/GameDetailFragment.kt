package com.dav1337d.catalog.ui.games.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
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
    ): View {

        val binding: GameDetailBinding = DataBindingUtil.inflate(
            inflater, R.layout.game_detail, container, false
        )

        val view: View = binding.root
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val id = args.gameId
        viewModel.setGame(id)
        return view
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity).supportActionBar?.show()
        viewModel.clear()
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.hide()
        toolbar.navigationIcon = resources.getDrawable(R.drawable.ic_baseline_arrow_back_24)
        toolbar.setNavigationOnClickListener(View.OnClickListener {
            findNavController().navigate(R.id.action_gameDetailFragment_to_games)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loading.observe(viewLifecycleOwner, Observer {
            if (it) {
                progress_loader.visibility = View.VISIBLE
            } else {
                progress_loader.visibility = View.GONE
                var fileName = (viewModel.game.value?.name + "backdrop" + ".png").replace("/", "")
                if (ImageSaver(App.appContext!!).setFileName(fileName)
                        .setDirectoryName("images").load() != null) {
                    backdrop.setImageBitmap(
                        ImageSaver(App.appContext!!).setFileName(fileName)
                            .setDirectoryName("images").load()
                    )
                } else {
                    fileName = (viewModel.game.value?.name + ".png").replace("/", "")
                    backdrop.setImageBitmap(
                        ImageSaver(App.appContext!!).setFileName(fileName)
                            .setDirectoryName("images").load()
                    )
                }

            }
        })
    }
}