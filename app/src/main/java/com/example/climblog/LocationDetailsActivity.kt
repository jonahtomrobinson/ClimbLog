package com.example.climblog

import android.app.AlertDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
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

        setSupportActionBar(findViewById(R.id.my_toolbar))
        supportActionBar?.title = intent.getStringExtra("locationName")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        //tv_location_name.text = FileHelper.nextId(FileHelper.getSetFilePath(applicationContext), "set").toString()

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
        rv_set_list.adapter = SetAdapter(sets, this)

        /** Listener for addLocation floating button.*/
        float_add_set.setOnClickListener { view ->
            val newIntent = Intent(this, AddClimbActivity::class.java).apply {
                putExtra("locationName", intent.getStringExtra("locationName"))
            }
            startActivity(newIntent)
        }
    }

    /** Inflate menu_actionbar for the action bar. */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_actionbar_trash, menu)
        return true
    }

    /** On detach, clear the locations array.*/
    override fun onPause() {
        super.onPause()
        sets.clear()
    }

    private fun addSets(parsedData: ArrayList<Set>?) {
        if (parsedData != null) {

            val sortedParsedData= parsedData.sortedWith(compareBy {it.date})

            for (set in sortedParsedData) {
                if (set.locationName == intent.getStringExtra("locationName")) {
                    sets.add(set)
                }
            }
        }
    }

    /** Inflate menu_actionbar for the action bar.
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_actionbar_trash, menu)
        return true
    }*/

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_trash -> {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Are you sure you wish to delete this location?")

            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                Toast.makeText(this.applicationContext,
                    "Deleted", Toast.LENGTH_SHORT).show()
                FileHelper.deleteData(intent.getStringExtra("locationName"), FileHelper.getLocationFilePath(applicationContext),"location" )
                val intent = Intent(this, NavigationActivity::class.java)
                startActivity(intent)

            }

            builder.setNegativeButton(android.R.string.no) { dialog, which ->

            }

            builder.create()
            builder.show()

            true

        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }
}
