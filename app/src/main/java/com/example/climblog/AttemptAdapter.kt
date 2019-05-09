package com.example.climblog

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.*
import kotlinx.android.synthetic.main.attempt_item.view.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.collections.ArrayList

/**
 * @desc Adapter class for attempt items. Handles assignment and formatting for the RecyclerView.
 * @author Jonah Robinson <jonahtomrobinson@gmail.com>
 * @date 07/05/2019
 */

class AttemptAdapter(val items: ArrayList<Completed>, val context: Context) : RecyclerView.Adapter<ViewHolder3>() {

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
        holder.tvNum?.text = (position + 1).toString()
        holder.tvDate?.text = LocalDateTime.parse(items[position].date).format(DateTimeFormatter.ISO_LOCAL_DATE)
        holder.tvAttempts?.text = items[position].attempts
    }
}

/** Using a ViewHolder grabs the data from the view and stores it in variables.*/
class ViewHolder3(view: View) : RecyclerView.ViewHolder(view) {
    val tvNum = view.tv_att_num
    val tvDate = view.tv_att_date
    val tvAttempts = view.tv_att_attempts

}
