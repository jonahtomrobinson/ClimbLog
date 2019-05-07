package com.example.climblog

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_location_details.*

class setDetailsActivity : AppCompatActivity() {

    private val routes: ArrayList<Set> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_details)

        setSupportActionBar(findViewById(R.id.my_toolbar))
        supportActionBar?.title = intent.getStringExtra("setLocation") + " " + intent.getStringExtra("setDiff")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onStart() {
        super.onStart()
        routes.clear()

        /** Loads locations from the JSON Location file into the local locations ArrayList.*/
        val setArray = FileHelper.parseJSON(FileHelper.getSetFilePath(applicationContext), "set")
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

    private fun addRoutes(parsedData: ArrayList<Set>?) {
        if (parsedData != null) {
            for (set in parsedData) {
                if (set.id == intent.getIntExtra("setId",0)) {
                    for (route in 1..set.routes) {
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

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_done -> {
            //val editText = findViewById<EditText>(R.id.signupEmail)
            //val message = editText.text.toString()
            val intent = Intent(this, NavigationActivity::class.java)
            startActivity(intent)
            true
        }
        android.R.id.home-> {
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

}
