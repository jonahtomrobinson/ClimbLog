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
import kotlinx.android.synthetic.main.activity_location_details.rv_set_list
import kotlinx.android.synthetic.main.activity_route_details.*

/**
 * @desc Activity which shows the "details" of a route, single climbs (routes).
 * @author Jonah Robinson <jonahtomrobinson@gmail.com>
 * @date 07/05/2019
 */

class RouteDetailsActivity : AppCompatActivity() {

    private val attempts: ArrayList<Completed> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route_details)

        /** Set the custom actionbar, page title, and actionbar title.*/
        setSupportActionBar(findViewById(R.id.my_toolbar))
        supportActionBar?.title = intent.getStringExtra("setLocation") + " " + intent.getStringExtra("setDiff")
        tv_identifier_title.text = intent.getStringExtra("setIdentifier")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onStart() {
        super.onStart()
        attempts.clear()

        /** Loads locations from the JSON Location file into the local locations ArrayList.*/
        val setArray = FileHelper.parseJSON("completed", FileHelper.getCompletedFilePath(applicationContext))
        if (!setArray.isEmpty() && setArray[0] is Completed) {
            addAttempts(setArray as ArrayList<Completed>)
        }

        //Creates a vertical Layout Manager
        rv_set_list.layoutManager = LinearLayoutManager(applicationContext)

        /** Access the RecyclerView Adapter and loads the local completed set array data into it.*/
        rv_set_list.adapter = AttemptAdapter(attempts, this)

    }

    /** On detach, clear the locations array.*/
    override fun onPause() {
        super.onPause()
        attempts.clear()
    }

    /** Adds the appropriate attempts to the ArrayList to be displayed */
    private fun addAttempts(parsedData: ArrayList<Completed>?) {
        if (parsedData != null) {
            for (item in parsedData) {
                if (item.setId == intent.getIntExtra("setId", 0)) {
                    if (item.routeNum > 0) {
                        if (item.routeNum == intent.getStringExtra("setIdentifier").toInt()) {
                            attempts.add(item)
                        }
                    } else {
                        attempts.add(item)
                    }
                }
            }
        }
    }

    /** Inflate menu_actionbar_trash for the action bar. */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (intent.getBooleanExtra("isATrueRoute", false)) {
            val inflater: MenuInflater = menuInflater
            inflater.inflate(R.menu.menu_actionbar_trash, menu)
        }
        return true
    }

    /** Action bar button listeners. */
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_trash -> {

            /** Deletion confirmation popup message.*/
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Are you sure you wish to delete this route?")

            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                showMessage("Deleted " + intent.getStringExtra("setIdentifier"))
                FileHelper.deleteData(
                    intent.getIntExtra("setId",-1),
                    "set",
                    FileHelper.getSetFilePath(applicationContext)
                )

                /** Delete all completed entries for the set. */
                FileHelper.deleteData(
                    intent.getIntExtra("setId",-1),
                    "completed",
                    FileHelper.getCompletedFilePath(applicationContext)
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
