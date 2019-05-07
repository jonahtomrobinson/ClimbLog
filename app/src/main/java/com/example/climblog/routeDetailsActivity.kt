package com.example.climblog

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_location_details.*
import kotlinx.android.synthetic.main.activity_location_details.rv_set_list
import kotlinx.android.synthetic.main.activity_route_details.*

class routeDetailsActivity : AppCompatActivity() {

    private val attempts: ArrayList<Completed> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route_details)

        setSupportActionBar(findViewById(R.id.my_toolbar))
        supportActionBar?.title = intent.getStringExtra("setLocation") + " " + intent.getStringExtra("setDiff")
        tv_identifier_title.text = intent.getStringExtra("setIdentifier")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onStart() {
        super.onStart()
        attempts.clear()

        /** Loads locations from the JSON Location file into the local locations ArrayList.*/
        val setArray = FileHelper.parseJSON(FileHelper.getCompletedFilePath(applicationContext), "completed")
        if (!setArray.isEmpty() && setArray[0] is Completed) {
            addRoutes(setArray as ArrayList<Completed>)
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

    private fun addRoutes(parsedData: ArrayList<Completed>?) {
        if (parsedData != null) {
            for (item in parsedData) {
                if (item.setId == intent.getIntExtra("setId",0)) {
                    if (item.routeNum > 0){
                        if(item.routeNum == intent.getStringExtra("setIdentifier").toInt()){
                            attempts.add(item)
                        }
                    }
                    else{
                        attempts.add(item)
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
