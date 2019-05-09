package com.example.climblog

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
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
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

/**
 * @desc Activity which shows the "details" of a location, e.g. the climbs (sets/routes) at the location.
 * @author Jonah Robinson <jonahtomrobinson@gmail.com>
 * @date 07/05/2019
 */

class LocationDetailsActivity : AppCompatActivity() {

    private val sets: ArrayList<Set> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_details)

        /** Set the custom actionbar and title.*/
        setSupportActionBar(findViewById(R.id.my_toolbar))
        supportActionBar?.title = intent.getStringExtra("locationName")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onStart() {
        super.onStart()
        sets.clear()

        /** Loads sets from the JSON Location file into the local locations ArrayList.*/
        val setArray = FileHelper.parseJSON("set", FileHelper.getSetFilePath(applicationContext))
        if (setArray.isNotEmpty() && setArray[0] is Set) {
            addSets(setArray as ArrayList<Set>)
        }

        /** Creates a vertical Layout Manager. */
        rv_set_list.layoutManager = LinearLayoutManager(applicationContext)

        /** Access the RecyclerView Adapter and loads the local locations array data into it.*/
        rv_set_list.adapter = SetAdapter(sets, this)

        /** Listener for addSet floating button.*/
        float_add_set.setOnClickListener { view ->
            val newIntent = Intent(this, AddClimbActivity::class.java).apply {
                putExtra("locationName", intent.getStringExtra("locationName"))
            }
            startActivity(newIntent)
        }

        val preferences = applicationContext.getSharedPreferences("com.example.app.STATE", Context.MODE_PRIVATE)
        if (preferences.getString("state", "") != "") {
            float_session.setImageResource(R.drawable.ic_timer_off)
        }

        /** Listener for session floating button.*/
        float_session.setOnClickListener { view ->
            val editor = preferences.edit()

            /** Starting a session. */
            if (preferences.getString("state", "") == "") {
                float_session.setImageResource(R.drawable.ic_timer_off)
                showMessage("Session started")
                editor.putString("state", LocalDateTime.now().toString())
                editor.apply()

                /** Refresh the recycle view.*/
                rv_set_list.adapter = SetAdapter(sets, this)
            }

            /** Ending a session. */
            else {
                float_session.setImageResource(R.drawable.ic_timer)
                showMessage("Session ended")
                editor.putString("state", "")
                editor.apply()

                /** Refresh the recycle view.*/
                rv_set_list.adapter = SetAdapter(sets, this)
            }
        }
    }

    /** Inflate menu_actionbar_trash for the action bar. */
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

            val sortedParsedData = parsedData.sortedWith(compareBy { it.date })

            for (set in sortedParsedData) {
                if (set.locationName == intent.getStringExtra("locationName")) {
                    sets.add(set)
                }
            }
        }
    }

    /** Action bar button listeners. */
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_trash -> {

            /** Deletion confirmation popup message.*/
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Are you sure you wish to delete this location?")

            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                showMessage("Deleted " + intent.getStringExtra("locationName"))

                val locationArray = FileHelper.parseJSON("location", FileHelper.getLocationFilePath(applicationContext))
                if (locationArray.isNotEmpty() && locationArray[0] is Location) {
                    val id = findIdFromName(locationArray as ArrayList<Location>)
                    if (id >= 0){

                        FileHelper.deleteData(
                            id,
                            "location",
                            FileHelper.getLocationFilePath(applicationContext)
                        )
                        val intent = Intent(this, NavigationActivity::class.java)
                        startActivity(intent)
                    }
                }

            }
            builder.setNegativeButton(android.R.string.no) { dialog, which -> }
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

    private fun findIdFromName(parsedData: ArrayList<Location>?): Int {
        if (parsedData != null) {
            for (location in parsedData) {
                if (location.name == intent.getStringExtra("locationName")) {
                    return location.id
                }
            }
        }
        return -1
    }

    /** Helper function for displaying toast popups. */
    private fun showMessage(message: String) {
        Toast.makeText(
            this, message,
            Toast.LENGTH_SHORT
        ).show()
    }
}
