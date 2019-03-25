package com.example.climblog

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class AddSetActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_set)

        /** Set the custom actionbar and title.*/
        setSupportActionBar(findViewById(R.id.my_toolbar))
        supportActionBar?.title = "Add Climb"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}
