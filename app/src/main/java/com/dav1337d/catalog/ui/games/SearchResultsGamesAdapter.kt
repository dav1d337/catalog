/*
* Copyright (C) 2014 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.dav1337d.catalog.ui.games

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dav1337d.catalog.App
import com.dav1337d.catalog.R
import com.dav1337d.catalog.extensions.timestampToDate
import com.dav1337d.catalog.model.games.GameDetailsResponse
import com.dav1337d.catalog.ui.base.OnClickListener
import com.dav1337d.catalog.util.ImageSaver


/**
 * Provide views to RecyclerView with data from dataSet.
 *
 * Initialize the dataset of the Adapter.
 *
 * @param dataSet String[] containing the data to populate views to be used by RecyclerView.
 */
class SearchResultsGamesAdapter(
    private var dataSet: List<GameDetailsResponse>,
    private val listener: OnClickListener<GameDetailsResponse>
) :
    RecyclerView.Adapter<SearchResultsGamesAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val textViewName: TextView
        val textViewYear: TextView
        val textViewDescription: TextView
        val imageView: ImageView
        val checkBox: ImageView

        init {
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener { Log.d(TAG, "Element $bindingAdapter clicked.") }
            textViewName = v.findViewById(R.id.name)
            textViewYear = v.findViewById(R.id.year)
            textViewDescription = v.findViewById(R.id.description)
            imageView = v.findViewById(R.id.imageView)
            checkBox = v.findViewById(R.id.checkBox)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view.
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.search_result_game_item, viewGroup, false)

        return ViewHolder(v)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.textViewName.text = dataSet[position].name
        viewHolder.textViewYear.text = dataSet[position].first_release_date.timestampToDate()
            .substring(
                dataSet[position].first_release_date.timestampToDate().lastIndex - 3,
                dataSet[position].first_release_date.timestampToDate().lastIndex + 1
            )
        viewHolder.textViewDescription.text = dataSet[position].summary
        val fileName = (dataSet[position].name + ".png").replace("/", "")
        viewHolder.imageView.setImageBitmap(
            ImageSaver(App.appContext!!).setFileName(fileName).setDirectoryName("images").load()
        )
        viewHolder.checkBox.setOnClickListener { listener.onCheckBoxClick(dataSet[position]) }
        dataSet[position].played?.let {
            if (it) {
                viewHolder.checkBox.setImageResource(R.drawable.ic_baseline_check_box_48_checked)
            } else {
                viewHolder.checkBox.setImageResource(R.drawable.ic_baseline_check_box_48_unchecked)
            }
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

    fun setItems(dataSet: List<GameDetailsResponse>) {
        this.dataSet = dataSet
        notifyDataSetChanged()
    }

    companion object {
        private val TAG = "SearchResultsTVAdapter"
    }
}
