package com.example.climblog

import android.app.AlertDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_location_details.*

/**
 * @desc Activity which shows the "details" of a set, a collection of routes (single climbs).
 * @author Jonah Robinson <jonahtomrobinson@gmail.com>
 * @date 07/05/2019
 */

class SetDetailsActivity : AppCompatActivity() {

    private val routes: ArrayList<Set> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_details)

        /** Set the custom actionbar and title.*/
        setSupportActionBar(findViewById(R.id.my_toolbar))
        supportActionBar?.title = intent.getStringExtra("setLocation") + " " + intent.getStringExtra("setDiff")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onStart() {
        super.onStart()
        routes.clear()

        /** Loads locations from the JSON Location file into the local locations ArrayList.*/
        val setArray = FileHelper.parseJSON("set", FileHelper.getSetFilePath(applicationContext))
        if (!setArray.isEmpty() && setArray[0] is Set) {
            addRoutes(setArray as ArrayList<Set>)
        }

        //Creates a vertical Layout Manager
        rv_set_list.layoutManager = LinearLayoutManager(applicationContext)

        /** Access the RecyclerView Adapter and loads the local locations array data into it.*/
        rv_set_list.adapter = SetAdapter(routes, this)

    }

    /** On detach, clear the locations array.*/
    override fun onPause() {
        super.onPause()
        routes.clear()
    }

    /** Adds the appropriate routes to the ArrayList to be displayed */
    private fun addRoutes(parsedData: ArrayList<Set>?) {
        if (parsedData != null) {
            for (set in parsedData) {
                if (set.id == intent.getIntExtra("setId", 0)) {
                    for (route in 1..set.routes) {

                        /** As routes are sets containing only a single route, create the temporary routes objects. */
                        routes.add(
                            Set(
                                set.id,
                                set.locationName,
                                set.difficulty,
                                set.colour,
                                route.toString(),
                                set.date,
                                -1
                            )
                        )
                    }
                }
            }
        }
    }

    /** Inflate menu_actionbar_trash for the action bar. */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_actionbar_trash, menu)
        return true
    }

    /** Action bar button listeners. */
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_trash -> {

            /** Deletion confirmation popup message.*/
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Are you sure you wish to delete this set?")

            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                showMessage("Deleted " + intent.getStringExtra("setIdentifier"))
                FileHelper.deleteData(
                    intent.getIntExtra("setId",-1),
                    "set",
                    FileHelper.getSetFilePath(applicationContext)
                )
                val intent = Intent(this, LocationDetailsActivity::class.java).apply {
                    putExtra("locationName", intent.getStringExtra("setLocation"))
                }
                startActivity(intent)

            }
            builder.setNegativeButton(android.R.string.no) { dialog, which -> }
            builder.create()
            builder.show()
            true
        }
        android.R.id.home -> {
            val intent = Intent(this, LocationDetailsActivity::class.java).apply {
                putExtra("locationName", intent.getStringExtra("setLocation"))
            }
            startActivity(intent)
            true
        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

    /** Helper function for displaying toast popups. */
    private fun showMessage(message: String) {
        Toast.makeText(
            this, message,
            Toast.LENGTH_SHORT
        ).show()
    }

}
