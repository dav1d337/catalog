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

package kinf.koch.catalog.ui

import android.content.Context
import android.media.Image
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.get
import androidx.core.view.size
import androidx.recyclerview.widget.RecyclerView
import kinf.koch.catalog.R
import kinf.koch.catalog.db.RoomSeriesMovie
import kinf.koch.catalog.util.ImageSaver


/**
 * Provide views to RecyclerView with data from dataSet.
 *
 * Initialize the dataset of the Adapter.
 *
 * @param dataSet String[] containing the data to populate views to be used by RecyclerView.
 */
class CustomAdapter internal constructor(
    context: Context
) :
        RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    private var dataSet = emptyList<RoomSeriesMovie>()
    var onItemLongClick: ((RoomSeriesMovie) -> Unit)? = null
    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val title: TextView
        val year: TextView
       // val watchDate: TextView
        val poster: ImageView
        val commentView: TextView
        val linearLayout: LinearLayout

        init {
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener { Log.d(TAG, "Element $adapterPosition clicked.") }
            v.setOnLongClickListener {
                onItemLongClick?.invoke(dataSet[adapterPosition])
                return@setOnLongClickListener false
            }
            title = v.findViewById(R.id.textView)
            year = v.findViewById(R.id.year)
            poster = v.findViewById(R.id.imageView2)
            commentView = v.findViewById(R.id.comment)
         //   watchDate = v.findViewById(R.id.watchDate)
            linearLayout = v.findViewById(R.id.linearLayout)
        }

    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view.
        val v = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.tv_cardview, viewGroup, false)

        return ViewHolder(v)
    }


    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        Log.d(TAG, "Element $position set.")

        viewHolder.title.text = dataSet[position].name
        viewHolder.year.text = dataSet[position].first_air_date.substring(0,4)
        val fileName = (dataSet[position].original_name + ".png").replace("/","")
        viewHolder.poster.setImageBitmap(ImageSaver(App.appContext!!).setFileName(fileName).setDirectoryName("images").load())
        val comment = dataSet[position].comment + " (" + dataSet[position].watchDate + ")"
        viewHolder.commentView.text = comment
        Log.i("hallo rating", dataSet[position].personalRating.toString())
        for (i in 0 until viewHolder.linearLayout.size) {
            if (dataSet[position].personalRating >= 0 && dataSet[position].personalRating > i) {
                viewHolder.linearLayout[i].visibility = View.VISIBLE
            } else {
                viewHolder.linearLayout[i].visibility = View.GONE
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return dataSet[position].personalRating
    }

    override fun getItemCount() = dataSet.size

    internal fun setItems(items: List<RoomSeriesMovie>) {
        this.dataSet = items
        notifyDataSetChanged()
    }

    companion object {
        private val TAG = "CustomAdapter"
    }
}
