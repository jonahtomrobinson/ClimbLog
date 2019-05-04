package com.example.climblog

import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.opengl.Visibility
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_climb.*
import kotlinx.android.synthetic.main.location_item.view.*
import kotlinx.android.synthetic.main.set_item.view.*
import java.time.Instant
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
        if (items[position].routes != 0){
            holder.tvType?.text = "Set"
            holder.tvIdentifier?.text = "1-"+items[position].routes.toString()
            holder.btnCompleted?.visibility = View.GONE
        }
        else{
            holder.tvType?.text = "Route"
            holder.tvIdentifier?.text = items[position].identifier

            var catch =  false
            val completedArray = FileHelper.parseJSON(FileHelper.getCompletedFilePath(context),"completed") as ArrayList<Completed>
            for (completed in completedArray){
                if (completed.setId == items[position].id){
                    holder.btnCompleted?.isChecked = true
                    holder.btnCompleted?.isClickable = false
                    catch = true
                }
            }

            if (!catch){
                holder.btnCompleted.setOnClickListener{
                    /*FileHelper.addData(
                        Completed(
                            FileHelper.nextId(FileHelper.getCompletedFilePath(context),"completed"),
                            items[position].id,
                            0,
                            Date.from(Instant.EPOCH).toString(),
                            "1"
                        ), "completed", FileHelper.getCompletedFilePath(context)
                    )*/

                    popup()
                    holder.btnCompleted?.isClickable = false
                }
            }

        }
        holder.tvDifficulty?.text = items[position].difficulty
        holder.itemView.setBackgroundColor(items[position].colour.toInt())

    }

    fun popup(){
        val builder = AlertDialog.Builder(context)

        builder.setTitle("Androidly Alert")
        builder.setMessage("We have a message")
        //builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            Toast.makeText(context.applicationContext,
                android.R.string.yes, Toast.LENGTH_SHORT).show()
        }

        builder.setNegativeButton(android.R.string.no) { dialog, which ->
            Toast.makeText(context.applicationContext,
                android.R.string.no, Toast.LENGTH_SHORT).show()
        }

        builder.show()
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
