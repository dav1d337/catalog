package com.dav1337d.catalog.ui.base

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.forEachIndexed
import androidx.fragment.app.DialogFragment
import com.dav1337d.catalog.R
import com.dav1337d.catalog.model.books.BookItem
import com.dav1337d.catalog.model.tv.EitherMovieOrSeries
import com.dav1337d.catalog.ui.base.OnClickListener
import kotlinx.android.synthetic.main.dialog_add_to_db.view.*
import java.text.SimpleDateFormat
import java.util.*

class AddToDbDialogFragment(val clickListener: OnClickListener<Any>, val item: Any): DialogFragment() {

    private var numberOfStars = 0

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.dialog_add_to_db, null)

            setupDatePicker(view)
            setupStars(view)

            builder
                .setView(view)
                .setPositiveButton("Save") { dialog, id ->
                    clickListener.onSaveClick(item, numberOfStars, view.datepicker.text.toString(), view.comment.text.toString())
                }
                .setNegativeButton("Cancel") { dialog, id ->
                    // User cancelled the dialog
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
        return dialog
    }

    private fun setupStars(view: View) {
        val stars = mutableListOf<ImageView>()
        view.starsLayout.forEachIndexed { indexOuter, star ->
            star.setOnClickListener {
                numberOfStars = 0
                stars.forEachIndexed { indexInner, star ->
                    if (indexInner <= indexOuter) {
                        star.setImageResource(R.drawable.ic_baseline_star_24)
                        numberOfStars++
                    } else {
                        star.setImageResource(R.drawable.ic_baseline_star_border_24)
                        // numberOfStars--
                    }
                }

            }
            stars.add(star as ImageView)
        }
    }

    private fun setupDatePicker(view: View) {
        val textView: TextView = view.findViewById(R.id.datepicker)
        textView.text = SimpleDateFormat("dd.MM.yyyy").format(System.currentTimeMillis())

        var cal = Calendar.getInstance()

        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val myFormat = "dd.MM.yyyy" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.GERMAN)
                textView.text = sdf.format(cal.time)
            }

        textView.setOnClickListener {
            DatePickerDialog(
                requireActivity(), dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }
}