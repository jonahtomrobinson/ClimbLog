package com.example.climblog

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_location_details.*
import kotlinx.android.synthetic.main.activity_navigation.*
import kotlinx.android.synthetic.main.fragment_indoor.*
import java.time.Instant
import java.util.*
import kotlin.collections.ArrayList

class LocationDetailsActivity : AppCompatActivity() {

    private val sets: ArrayList<Set> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_details)


        tv_location_name.text = intent.getStringExtra("locationName")
        tv_location_name.text = FileHelper.nextId(FileHelper.getSetFilePath(applicationContext), "set").toString()

        //FileHelper.addData(Set( FileHelper.nextId(FileHelper.getSetFilePath(applicationContext),"set"), intent.getStringExtra("locationName"), "V2", "Black", "1 - 8", Date.from(Instant.EPOCH).toString()),"set",FileHelper.getSetFilePath(applicationContext))
    }

    override fun onStart() {
        super.onStart()
        sets.clear()

        /** Loads locations from the JSON Location file into the local locations ArrayList.*/
        val setArray = FileHelper.parseJSON(FileHelper.getSetFilePath(applicationContext), "set")
        if (!setArray.isEmpty() && setArray[0] is Set) {
            addSets(setArray as ArrayList<Set>)
        }

        //Creates a vertical Layout Manager
        rv_set_list.layoutManager = LinearLayoutManager(applicationContext)

        /** Access the RecyclerView Adapter and loads the local locations array data into it.*/
        rv_set_list.adapter = SetAdapter(sets, applicationContext)

    }

    /** On detach, clear the locations array.*/
    override fun onPause() {
        super.onPause()
        sets.clear()
    }

    private fun addSets(parsedData: ArrayList<Set>?) {
        if (parsedData != null) {
            for (set in parsedData) {
                if (set.locationName == intent.getStringExtra("locationName")) {
                    sets.add(set)
                }
            }
        }
    }
}
