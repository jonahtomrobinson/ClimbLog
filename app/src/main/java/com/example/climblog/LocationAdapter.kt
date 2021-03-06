package com.example.climblog

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.location_item.view.*

class LocationAdapter(val items : ArrayList<Location>, val context: Context) : RecyclerView.Adapter<ViewHolder>() {

    /** Gets the number of locations in the list.*/
    override fun getItemCount(): Int {
        return items.size
    }

    /** Inflates the item views.*/
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.location_item, parent, false))
    }

    /** Binds each location in the ArrayList to a view.*/
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvLocationName?.text = items[position].name
        holder.tvLocationAddress?.text = items[position].address
        holder.tvLocationLastVisited?.text = "Last Visited\n" + items[position].lastVisited

        /** Determine and correctly display favourite locations.*/
        if (items[position].favourite) {
            holder.tvLocationFavourite?.setImageResource(R.drawable.ic_star_filled)
        }
        else{
            holder.tvLocationFavourite?.setImageResource(R.drawable.ic_star_clear)
        }

        /** On click of a location, open the location details page.*/
        holder.itemView.setOnClickListener {
            val intent = Intent(this.context, LocationDetailsActivity::class.java).apply {
                putExtra("locationName", items[position].name)
            }
            context.startActivity(intent)
        }

        val updateMap: MutableMap<String, String> = mutableMapOf()

        /** On click of the favourite star, update the JSON data accordingly.*/
        holder.tvLocationFavourite.setOnClickListener{
            if (items[position].favourite){
                updateMap.put("favourite","false")
                holder.tvLocationFavourite?.setImageResource(R.drawable.ic_star_clear)
                items[position].favourite = false
            }
            else{
                updateMap.put("favourite","true")
                holder.tvLocationFavourite?.setImageResource(R.drawable.ic_star_filled)
                items[position].favourite = true
            }
            FileHelper.updateData(items[position].name,FileHelper.getLocationFilePath(context),"location", updateMap)
            //Toast.makeText(context, updateMap.get("favourite"), Toast.LENGTH_SHORT).show()
            updateMap.clear()
        }

    }
}

/** Using a ViewHolder grabs the data from the view and stores it in variables.*/
class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    val tvLocationName = view.tv_location_name
    val tvLocationAddress = view.tv_location_address
    val tvLocationLastVisited = view.tv_location_last_visited
    val tvLocationFavourite = view.tv_location_favourite
}