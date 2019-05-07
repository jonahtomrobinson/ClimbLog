package com.example.climblog

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v4.graphics.ColorUtils
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_route_details.view.*
import kotlinx.android.synthetic.main.attempt_item.view.*
import kotlinx.android.synthetic.main.dialog_tries.view.*
import kotlinx.android.synthetic.main.set_item.view.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.collections.ArrayList

class AttemptAdapter(val items : ArrayList<Completed>, val context: Context) : RecyclerView.Adapter<ViewHolder3>() {

    /** Gets the number of locations in the list.*/
    override fun getItemCount(): Int {
        return items.size
    }

    /** Inflates the item views.*/
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder3 {
        return ViewHolder3(LayoutInflater.from(context).inflate(R.layout.attempt_item, parent, false))
    }

    /** Binds each location in the ArrayList to a view.*/
    override fun onBindViewHolder(holder: ViewHolder3, position: Int) {
            holder.tvNum?.text = (position+1).toString()
            holder.tvDate?.text = LocalDateTime.parse( items[position].date).format(DateTimeFormatter.ISO_LOCAL_DATE)
            holder.tvAttempts?.text = items[position].attempts
    }
}

/** Using a ViewHolder grabs the data from the view and stores it in variables.*/
class ViewHolder3(view: View) : RecyclerView.ViewHolder(view) {
    val tvNum = view.tv_att_num
    val tvDate = view.tv_att_date
    val tvAttempts = view.tv_att_attempts

}
