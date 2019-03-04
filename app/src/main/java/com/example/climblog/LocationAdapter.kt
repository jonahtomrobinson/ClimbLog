package com.example.climblog

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.location_item.view.*

class LocationAdapter(val items : ArrayList<Location>, val context: Context) : RecyclerView.Adapter<ViewHolder>() {

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return items.size
    }

    // Inflates the item views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.location_item, parent, false))
    }

    // Binds each animal in the ArrayList to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.tvLocationName?.text = items[position].name
        holder?.tvLocationAddress?.text = items[position].address
        holder?.tvLocationLastVisited?.text = items[position].lastVisited
    }
}

class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    // Holds the TextView that will add each animal to
    val tvLocationName = view.tv_location_name
    val tvLocationAddress = view.tv_location_address
    val tvLocationLastVisited = view.tv_location_last_visited
}