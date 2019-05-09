package com.example.climblog

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v4.graphics.ColorUtils
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.Toast
import kotlinx.android.synthetic.main.dialog_tries.view.*
import kotlinx.android.synthetic.main.set_item.view.*
import java.time.LocalDateTime
import kotlin.collections.ArrayList

/**
 * @desc Adapter class for set items. Handles assignment and formatting for the RecyclerView.
 * @author Jonah Robinson <jonahtomrobinson@gmail.com>
 * @date 07/05/2019
 */

class SetAdapter(val items: ArrayList<Set>, val context: Context) : RecyclerView.Adapter<ViewHolder2>() {

    /** Gets the number of locations in the list.*/
    override fun getItemCount(): Int {
        return items.size
    }

    /** Inflates the item views.*/
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder2 {
        return ViewHolder2(LayoutInflater.from(context).inflate(R.layout.set_item, parent, false))
    }

    /** Binds each location in the ArrayList to a view.*/
    override fun onBindViewHolder(holder: ViewHolder2, position: Int) {
        if (items[position].routes > 0) {
            holder.tvType?.text = "Set"
            holder.tvIdentifier?.text = "1-" + items[position].routes.toString()
            holder.btnCompleted?.visibility = View.GONE

            /** On click of a set, open the set details page.*/
            holder.itemView.setOnClickListener {
                val intent = Intent(this.context, SetDetailsActivity::class.java).apply {
                    putExtra("setLocation", items[position].locationName)
                    putExtra("setDiff", items[position].difficulty)
                    putExtra("setId", items[position].id)
                    putExtra("setIdentifier", items[position].identifier)
                }
                context.startActivity(intent)
            }

        } else {
            holder.tvType?.text = "Route"
            holder.tvIdentifier?.text = items[position].identifier

            /** Grab the current session state. */
            val preferences =
                context.applicationContext.getSharedPreferences("com.example.app.STATE", Context.MODE_PRIVATE)
            val state = preferences.getString("state", "")

            var catch = false
            val completedArray =
                FileHelper.parseJSON("completed", FileHelper.getCompletedFilePath(context)) as ArrayList<Completed>
            for (completed in completedArray) {

                /** If a completed entry is found for the item AND (either there is currently no session
                 * OR the item has been completed this session). THEN set the checkbox to TRUE */
                if (completed.setId == items[position].id &&
                    (state == "" || LocalDateTime.parse(state).isBefore(LocalDateTime.parse(completed.date)))
                ) {
                    if (items[position].routes < 0) {
                        if (completed.routeNum == items[position].identifier.toInt()) {
                            holder.btnCompleted?.isChecked = true
                            holder.btnCompleted?.isClickable = false
                            catch = true
                        }
                    } else {
                        holder.btnCompleted?.isChecked = true
                        holder.btnCompleted?.isClickable = false
                        catch = true
                    }
                }
            }

            if (!catch) {
                /** On checkbox button click, call the popup function for confirmation of attempts. */
                holder.btnCompleted.setOnClickListener {
                    popup(holder, items[position])
                }
            }

            /** On click of a set, open the set details page.*/
            holder.itemView.setOnClickListener {
                val intent = Intent(this.context, RouteDetailsActivity::class.java).apply {
                    putExtra("setLocation", items[position].locationName)
                    putExtra("setDiff", items[position].difficulty)
                    putExtra("setId", items[position].id)
                    putExtra("setIdentifier", items[position].identifier)
                    if(items[position].routes < 0){
                        putExtra("isATrueRoute", false)
                    }
                    else{
                        putExtra("isATrueRoute", true)
                    }
                }
                context.startActivity(intent)
            }
        }
        holder.tvDifficulty?.text = items[position].difficulty
        holder.itemView.setBackgroundColor(items[position].colour.toInt())

        /** Using the luminance of the selected colour, detect whether to display dark text (default) or light text. */
        if (ColorUtils.calculateLuminance(items[position].colour.toInt()) < 0.5 &&
            ColorUtils.calculateLuminance(items[position].colour.toInt()) != 0.0
        ) {
            holder.tvType?.setTextColor(Color.WHITE)
            holder.tvDifficulty?.setTextColor(Color.WHITE)
            holder.tvIdentifier?.setTextColor(Color.WHITE)
        }
    }

    /** Popup for displaying and selecting the number of attempts taken*/
    fun popup(holder: ViewHolder2, set: Set) {
        val builder = AlertDialog.Builder(context)
        val inflator = LayoutInflater.from(context)

        val mDialogView = inflator.inflate(R.layout.dialog_tries, null)
        builder.setView(mDialogView)

        /** Formatting selected popup choice button (and other buttons) appropriately. */
        var triesValue = ""
        mDialogView.rb_1.setOnClickListener {
            wipePopupButtons(mDialogView)
            mDialogView.rb_1.setBackgroundResource(R.color.colorOrange)
            triesValue = "1"
        }
        mDialogView.rb_2.setOnClickListener {
            wipePopupButtons(mDialogView)
            mDialogView.rb_2.setBackgroundResource(R.color.colorOrange)
            triesValue = "2"
        }
        mDialogView.rb_3.setOnClickListener {
            wipePopupButtons(mDialogView)
            mDialogView.rb_3.setBackgroundResource(R.color.colorOrange)
            triesValue = "3"
        }
        mDialogView.rb_4.setOnClickListener {
            wipePopupButtons(mDialogView)
            mDialogView.rb_4.setBackgroundResource(R.color.colorOrange)
            triesValue = "4+"
        }

        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            Toast.makeText(
                context.applicationContext,
                set.identifier + " completed", Toast.LENGTH_SHORT
            ).show()
            holder.btnCompleted?.isClickable = false
            holder.btnCompleted?.isChecked = true

            /** If the route is a member of a set, include its routeNum in the completed entry. */
            var routeNum = 0
            if (set.routes < 0) {
                routeNum = set.identifier.toInt()
            }

            /** Add new Completed entry to the JSONfile. */
            FileHelper.addData(
                Completed(
                    FileHelper.nextId("completed", FileHelper.getCompletedFilePath(context)),
                    set.id,
                    routeNum,
                    LocalDateTime.now().toString(),
                    triesValue
                ), "completed", FileHelper.getCompletedFilePath(context)
            )
        }

        builder.setNegativeButton(android.R.string.no) { dialog, which ->
            holder.btnCompleted?.isClickable = true
            holder.btnCompleted?.isChecked = false
        }
        builder.create()
        builder.show()

    }

    /** removes formatting on all popup choice buttons. */
    private fun wipePopupButtons(mDialogView: View) {
        mDialogView.rb_1.setBackgroundResource(R.color.colorWhite)
        mDialogView.rb_2.setBackgroundResource(R.color.colorWhite)
        mDialogView.rb_3.setBackgroundResource(R.color.colorWhite)
        mDialogView.rb_4.setBackgroundResource(R.color.colorWhite)
    }
}

/** Using a ViewHolder grabs the data from the view and stores it in variables.*/
class ViewHolder2(view: View) : RecyclerView.ViewHolder(view) {
    val tvType = view.tv_type
    val tvColor = view.tv_set_color
    val tvDifficulty = view.tv_difficulty
    val tvIdentifier = view.tv_identifier
    val btnCompleted = view.btn_completed

}
