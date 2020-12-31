package com.dav1337d.catalog.ui.books

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.get
import androidx.core.view.size
import androidx.recyclerview.widget.RecyclerView
import com.dav1337d.catalog.App
import com.dav1337d.catalog.R
import com.dav1337d.catalog.db.RoomBook
import com.dav1337d.catalog.util.ImageSaver


/**
 * Provide views to RecyclerView with data from dataSet.
 *
 * Initialize the dataset of the Adapter.
 *
 * @param dataSet String[] containing the data to populate views to be used by RecyclerView.
 */
class CustomBooksAdapter internal constructor(
    context: Context
) :
    RecyclerView.Adapter<CustomBooksAdapter.ViewHolder>() {

    private var dataSet = emptyList<RoomBook>()
    var onItemLongClick: ((RoomBook) -> Unit)? = null

    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val title: TextView
        val subtitle: TextView

        //  val year: TextView
        // val watchDate: TextView
        val poster: ImageView
        val commentView: TextView
        val linearLayout: LinearLayout

        init {
            v.setOnLongClickListener {
                onItemLongClick?.invoke(dataSet[bindingAdapterPosition])
                return@setOnLongClickListener false
            }
            title = v.findViewById(R.id.book_title)
            subtitle = v.findViewById(R.id.book_subtitle)
            //  year = v.findViewById(R.id.book_year)
            poster = v.findViewById(R.id.book_img)
            commentView = v.findViewById(R.id.book_comment)
            linearLayout = v.findViewById(R.id.book_linearLayout)
        }

    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view.
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.book_cardview, viewGroup, false)

        return ViewHolder(v)
    }


    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        Log.d(TAG, "Element $position set.")

        viewHolder.title.text =
            dataSet[position].title + " (" + dataSet[position].year?.substring(0, 4) + ")"
        viewHolder.subtitle.text = dataSet[position].subtitle
        val comment = dataSet[position].comment + " (" + dataSet[position].readDate + ")"
        viewHolder.commentView.text = comment

        val fileName = (dataSet[position].title + ".png").replace("/", "")
        viewHolder.poster.setImageBitmap(
            ImageSaver(App.appContext!!).setFileName(fileName).setDirectoryName("images").load()
        )

        for (i in 0 until viewHolder.linearLayout.size) {
            if (dataSet[position].personalRating >= 0 && dataSet[position].personalRating > i) {
                viewHolder.linearLayout[i].visibility = View.VISIBLE
            } else {
                viewHolder.linearLayout[i].visibility = View.GONE
            }
        }
    }

    override fun getItemCount() = dataSet.size

    internal fun setItems(items: List<RoomBook>) {
        this.dataSet = items
        notifyDataSetChanged()
    }

    companion object {
        private val TAG = "CustomBooksAdapter"
    }
}
