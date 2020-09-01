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

package kinf.koch.catalog.ui.tv

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kinf.koch.catalog.R
import kinf.koch.catalog.model.tv.EitherMovieOrSeries


/**
 * Provide views to RecyclerView with data from dataSet.
 *
 * Initialize the dataset of the Adapter.
 *
 * @param dataSet String[] containing the data to populate views to be used by RecyclerView.
 */
class SearchResultsAdapter(private var dataSet: List<EitherMovieOrSeries>, private val listener: OnClickListener) :
        RecyclerView.Adapter<SearchResultsAdapter.ViewHolder>() {

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
            v.setOnClickListener { Log.d(TAG, "Element $adapterPosition clicked.") }
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
                .inflate(R.layout.search_result_tv_item, viewGroup, false)

        return ViewHolder(v)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        Log.d(TAG, "Element $position set.")

        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        viewHolder.textViewName.text = dataSet[position].name
        if (dataSet[position].first_air_date.length >= 4) { viewHolder.textViewYear.text = dataSet[position].first_air_date.substring(0,4) }
        viewHolder.textViewDescription.text = dataSet[position].overview
        viewHolder.imageView.setImageBitmap(dataSet[position].poster)
        viewHolder.checkBox.setOnClickListener { listener.onCheckBoxClick(dataSet[position]) }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size


    fun setItems(dataSet: List<EitherMovieOrSeries>) {
        this.dataSet = dataSet
        notifyDataSetChanged()
    }

    companion object {
        private val TAG = "CustomAdapter"
    }
}
