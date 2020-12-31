package com.dav1337d.catalog.ui.games

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
import com.dav1337d.catalog.db.RoomGame
import com.dav1337d.catalog.extensions.timestampToDate
import com.dav1337d.catalog.util.ImageSaver


/**
 * Provide views to RecyclerView with data from dataSet.
 *
 * Initialize the dataset of the Adapter.
 *
 * @param dataSet String[] containing the data to populate views to be used by RecyclerView.
 */
class CustomGamesAdapter internal constructor(
    context: Context
) :
    RecyclerView.Adapter<CustomGamesAdapter.ViewHolder>() {

    private var dataSet = emptyList<RoomGame>()
    var onItemLongClick: ((RoomGame) -> Unit)? = null

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val title: TextView

        // val year: TextView
        // val watchDate: TextView
        val poster: ImageView
        val commentView: TextView
        val linearLayout: LinearLayout

        init {
            v.setOnLongClickListener {
                onItemLongClick?.invoke(dataSet[bindingAdapterPosition])
                return@setOnLongClickListener false
            }
            title = v.findViewById(R.id.textView)
            poster = v.findViewById(R.id.imageView2)
            commentView = v.findViewById(R.id.comment)
            linearLayout = v.findViewById(R.id.tv_linearLayout_stars)
        }

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.game_cardview, viewGroup, false)

        return ViewHolder(v)
    }


    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        Log.d(TAG, "Element $position set.")

        viewHolder.title.text =
            dataSet[position].name + " (" + dataSet[position].first_release_date?.timestampToDate()
                ?.substring(
                    dataSet[position].first_release_date!!.timestampToDate()?.lastIndex - 3,
                    dataSet[position].first_release_date!!.timestampToDate()?.lastIndex + 1
                ) + ")"
        val fileName = (dataSet[position].name + ".png").replace("/", "")
        viewHolder.poster.setImageBitmap(
            ImageSaver(App.appContext!!).setFileName(fileName).setDirectoryName("images").load()
        )
        val comment = dataSet[position].comment + " (" + dataSet[position].playDate + ")"
        viewHolder.commentView.text = comment

        for (i in 0 until viewHolder.linearLayout.size) {
            if (dataSet[position].personalRating >= 0 && dataSet[position].personalRating > i) {
                viewHolder.linearLayout[i].visibility = View.VISIBLE
            } else {
                viewHolder.linearLayout[i].visibility = View.GONE
            }
        }
    }

//    override fun getItemViewType(position: Int): Int {
//        return dataSet[position].personalRating
//    }

    override fun getItemCount() = dataSet.size

    internal fun setItems(items: List<RoomGame>) {
        this.dataSet = items
        notifyDataSetChanged()
    }

    companion object {
        private val TAG = "CustomAdapter"
    }
}
