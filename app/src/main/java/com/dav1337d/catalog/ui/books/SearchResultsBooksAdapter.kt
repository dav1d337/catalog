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

package com.dav1337d.catalog.ui.books

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dav1337d.catalog.R
import com.dav1337d.catalog.model.books.BookItem
import com.dav1337d.catalog.ui.base.OnClickListener

/**
 * Provide views to RecyclerView with data from dataSet.
 *
 * Initialize the dataset of the Adapter.
 *
 * @param dataSet String[] containing the data to populate views to be used by RecyclerView.
 */
class SearchResultsBooksAdapter(
    private var dataSet: List<BookItem>,
    private val listener: OnClickListener<BookItem>
) :
    RecyclerView.Adapter<SearchResultsBooksAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val textViewTitle: TextView
        val textViewYear: TextView
        val textViewAuthor: TextView
        val textViewDescription: TextView
        val imageView: ImageView
        val checkBox: ImageView

        init {
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener { Log.d(TAG, "Element $bindingAdapterPosition clicked.") }
            textViewTitle = v.findViewById(R.id.name)
            textViewYear = v.findViewById(R.id.year)
            textViewAuthor = v.findViewById(R.id.author)
            textViewDescription = v.findViewById(R.id.description)
            imageView = v.findViewById(R.id.imageView)
            checkBox = v.findViewById(R.id.checkBox)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view.
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.search_result_book_item, viewGroup, false)

        return ViewHolder(v)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        Log.d(TAG, "Element $position set.")

        viewHolder.textViewTitle.text = dataSet[position].volumeInfo.title
        viewHolder.textViewAuthor.text = dataSet[position].volumeInfo.authors.toString()
        Log.i("hallo s1", dataSet[position].volumeInfo.title)
        if (dataSet[position].volumeInfo.publishedDate != null) {
            if (dataSet[position].volumeInfo.publishedDate!!.length >= 4) {
                viewHolder.textViewYear.text =
                    dataSet[position].volumeInfo.publishedDate?.substring(0, 4)
            }
        }

        viewHolder.textViewDescription.text = dataSet[position].volumeInfo.description
        viewHolder.imageView.setImageBitmap(dataSet[position].thumbnail)
        viewHolder.checkBox.setOnClickListener { listener.onCheckBoxClick(dataSet[position]) }

        dataSet[position].read.let {
            if (it) {
                viewHolder.checkBox.setImageResource(R.drawable.ic_baseline_check_box_48_checked)
            } else {
                viewHolder.checkBox.setImageResource(R.drawable.ic_baseline_check_box_48_unchecked)
            }
        }

    }

    override fun getItemCount() = dataSet.size


    fun setItems(dataSet: List<BookItem>) {
        this.dataSet = dataSet
        notifyDataSetChanged()
    }

    companion object {
        private val TAG = "SearchResultsBookAdapter"
    }
}
