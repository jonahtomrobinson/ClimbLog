package com.example.climblog

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_indoor.*
import kotlin.collections.ArrayList

class IndoorFragment : Fragment() {
    companion object {
        fun newInstance(): IndoorFragment {
            return IndoorFragment()
        }
    }

    /** Local locations array which stores locations to be displayed*/
    private val locations: ArrayList<Location> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_indoor, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        locations.clear()

        /** Loads locations from the JSON Location file into the local locations ArrayList.*/
        val locationArray = FileHelper.parseJSON(FileHelper.getLocationFilePath(context!!), "location")
        if (!locationArray.isEmpty() && locationArray[0] is Location){
            addLocations(locationArray as ArrayList<Location>)
        }

        //Creates a vertical Layout Manager
        rv_location_list.layoutManager = LinearLayoutManager(activity!!.applicationContext)

        /** Access the RecyclerView Adapter and loads the local locations array data into it.*/
        rv_location_list.adapter = LocationAdapter(locations, context!!)

        /** Listener for addLocation floating button.*/
        float_add_location.setOnClickListener { view ->
            val intent = Intent(activity, AddLocationActivity::class.java)
            startActivity(intent)
        }

    }

    /** On detach, clear the locations array.*/
    override fun onDetach() {
        super.onDetach()
        locations.clear()
    }

    /** Adds Locations into the local locations array, after sorting alphabetically and by favourites.*/
    private fun addLocations(parsedData: ArrayList<Location>?) {
        if (parsedData != null) {

            var sortedParsedData= parsedData.sortedWith(compareBy{it.name})
            sortedParsedData = sortedParsedData.sortedWith(compareByDescending {it.favourite})

            for (location in sortedParsedData) {
                if (location.inOrOut == "indoor"){
                    locations.add(location)
                }
            }
        }
    }

}
