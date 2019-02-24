package com.example.climblog

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_indoor.*

class IndoorFragment : Fragment() {
    companion object {
        fun newInstance(): IndoorFragment {
            return IndoorFragment()
        }
    }

    val locations: ArrayList<String> = ArrayList()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_indoor, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // Loads locations into the ArrayList
        addLocations()

        // Creates a vertical Layout Manager
        rv_location_list.layoutManager = LinearLayoutManager(activity!!.applicationContext)
        // Access the RecyclerView Adapter and load the data into it
        rv_location_list.adapter = LocationAdapter(locations,  context!! )
    }


    fun addLocations() {
        locations.add("dog")
        locations.add("cat")
        locations.add("owl")
        locations.add("cheetah")
        locations.add("raccoon")
        locations.add("bird")
        locations.add("snake")
        locations.add("lizard")
        locations.add("hamster")
        locations.add("bear")
        locations.add("lion")
        locations.add("tiger")
    }
}
