package kinf.koch.catalog.ui.tv

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.core.view.forEach
import androidx.core.view.forEachIndexed
import androidx.fragment.app.DialogFragment
import io.reactivex.internal.subscriptions.SubscriptionHelper.cancel
import kinf.koch.catalog.R
import kotlinx.android.synthetic.main.dialog_add_to_db.*
import kotlinx.android.synthetic.main.dialog_add_to_db.view.*

class AddToDbDialogFragment : DialogFragment() {

    var numberOfStars = 0

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.dialog_add_to_db, null)

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
                            numberOfStars--
                        }
                    }

                }
                stars.add(star as ImageView)
            }

            builder
                .setView(view)
                .setPositiveButton("Save", { dialog, id ->
                    Log.i("hallo", numberOfStars.toString())
                    // FIRE ZE MISSILES!
                })
                .setNegativeButton("Cancel", { dialog, id ->
                    // User cancelled the dialog
                })
            // Create the AlertDialog object
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
        return dialog
    }
}