package com.example.climblog

import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.opengl.Visibility
import android.support.v4.content.res.ResourcesCompat
import android.support.v4.graphics.ColorUtils
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_climb.*
import kotlinx.android.synthetic.main.dialog_tries.view.*
import kotlinx.android.synthetic.main.location_item.view.*
import kotlinx.android.synthetic.main.set_item.view.*
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

class SetAdapter(val items : ArrayList<Set>, val context: Context) : RecyclerView.Adapter<ViewHolder2>() {

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
        if (items[position].routes > 0){
            holder.tvType?.text = "Set"
            holder.tvIdentifier?.text = "1-"+items[position].routes.toString()
            holder.btnCompleted?.visibility = View.GONE

            /** On click of a set, open the set details page.*/
            holder.itemView.setOnClickListener {
                val intent = Intent(this.context, setDetailsActivity::class.java).apply {
                    putExtra("setInfo", items[position].locationName+" "+items[position].difficulty)
                    putExtra("setId", items[position].id)
                }
                context.startActivity(intent)
            }

        }
        else{
            holder.tvType?.text = "Route"
            holder.tvIdentifier?.text = items[position].identifier

            var catch =  false
            val completedArray = FileHelper.parseJSON(FileHelper.getCompletedFilePath(context),"completed") as ArrayList<Completed>
            for (completed in completedArray){
                if (completed.setId == items[position].id){
                    if (items[position].routes < 0){
                        if (completed.routeNum == items[position].identifier.toInt()){
                            holder.btnCompleted?.isChecked = true
                            holder.btnCompleted?.isClickable = false
                            catch = true
                        }
                    }else{
                        holder.btnCompleted?.isChecked = true
                        holder.btnCompleted?.isClickable = false
                        catch = true
                    }
                }
            }

            if (!catch){
                holder.btnCompleted.setOnClickListener{
                    popup(holder,items[position])
                }
            }
        }
        holder.tvDifficulty?.text = items[position].difficulty
        holder.itemView.setBackgroundColor(items[position].colour.toInt())

        /** Using the luminance of the selected colour, detect whether to display dark text (default) or light text. */
        if (ColorUtils.calculateLuminance(items[position].colour.toInt()) < 0.5 && ColorUtils.calculateLuminance(items[position].colour.toInt()) != 0.0){
            holder.tvType?.setTextColor(Color.WHITE)
            holder.tvDifficulty?.setTextColor(Color.WHITE)
            holder.tvIdentifier?.setTextColor(Color.WHITE)
            //holder.btnCompleted?.setBackgroundColor(Color.WHITE)
        }
    }

    /** Popup for displaying and selecting the number of attempts taken*/
    fun popup(holder: ViewHolder2, set:Set){
        val builder = AlertDialog.Builder(context)
        val inflator = LayoutInflater.from(context)

        val mDialogView = inflator.inflate(R.layout.dialog_tries, null)
        builder.setView(mDialogView)

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
            Toast.makeText(context.applicationContext,
                set.identifier+" completed", Toast.LENGTH_SHORT).show()
            holder.btnCompleted?.isClickable = false
            holder.btnCompleted?.isChecked = true

            /** If the route is a member of a set, include its routeNum in the completed entry. */
            var routeNum = 0
            if (set.routes < 0){
                routeNum = set.identifier.toInt()
            }

            FileHelper.addData(
                Completed(
                    FileHelper.nextId(FileHelper.getCompletedFilePath(context),"completed"),
                    set.id,
                    routeNum,
                    LocalDate.now().toString(),
                    triesValue
                ), "completed", FileHelper.getCompletedFilePath(context))
        }

        builder.setNegativeButton(android.R.string.no) { dialog, which ->
            holder.btnCompleted?.isClickable = true
            holder.btnCompleted?.isChecked = false
        }
        builder.create()
        builder.show()

    }

    private fun wipePopupButtons (mDialogView : View){
        mDialogView.rb_1.setBackgroundResource(R.color.colorWhite)
        mDialogView.rb_2.setBackgroundResource(R.color.colorWhite)
        mDialogView.rb_3.setBackgroundResource(R.color.colorWhite)
        mDialogView.rb_4.setBackgroundResource(R.color.colorWhite)
    }
}

/** Using a ViewHolder grabs the data from the view and stores it in variables.*/
class ViewHolder2(view: View) : RecyclerView.ViewHolder(view) {
    val tvType = view.tv_set_type
    val tvColor = view.tv_set_color
    val tvDifficulty = view.tv_set_difficulty
    val tvIdentifier = view.tv_set_identifier
    val btnCompleted = view.btn_completed

}
