package com.example.climblog

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.location_item.view.*
import kotlinx.android.synthetic.main.set_item.view.*

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
        holder.tvSetId?.text = items[position].id.toString()
        holder.tvLocationName?.text = items[position].locationName

    }
}

/** Using a ViewHolder grabs the data from the view and stores it in variables.*/
class ViewHolder2(view: View) : RecyclerView.ViewHolder(view) {
    val tvSetId = view.tv_set_id
    val tvLocationName = view.tv_set_location_name

}
