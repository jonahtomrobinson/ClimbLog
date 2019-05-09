package com.example.climblog

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_location.*

const val EXTRA_NAV_ARRAY = "come.example.climblog.NAV_ARRAY"

/**
 * @desc Form for adding new locations.
 * @author Jonah Robinson <jonahtomrobinson@gmail.com>
 * @date 07/05/2019
 */

class AddLocationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_location)

        /** Set the custom actionbar and title.*/
        setSupportActionBar(findViewById(R.id.my_toolbar))
        supportActionBar?.title = "Add Location"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    /** Inflate menu_actionbar for the action bar.*/
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_actionbar, menu)
        return true
    }

    /** On confirm creation of a new location.*/
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_done -> {
            val locationName = input_location_name.text.toString()
            if (locationName != "") {

                /** Using locationName grab the stored Locations JSON as an array.*/
                val storedLocations =
                    FileHelper.parseJSON("location", FileHelper.getLocationFilePath(this)) as ArrayList<Location>

                /** Testing whether the name is duplicated.*/
                var duplicateName = false
                for (location in storedLocations) {
                    if (location.name == locationName) {
                        duplicateName = true
                    }
                }

                /** If duplicated alert user, otherwise add location.*/
                if (duplicateName) {
                    Toast.makeText(this, "Duplicate location name entered.", Toast.LENGTH_LONG).show()
                } else {

                    /** Build message to pass to create new location function. */
                    val message = ArrayList<String>()
                    message.add("indoor")
                    message.add(locationName)
                    message.add(input_location_address.text.toString())
                    message.add("")

                    if (switch_favourite.isChecked) {
                        message.add("true")
                    } else {
                        message.add("false")
                    }

                    if (inOutRadioGroup.checkedRadioButtonId == radio_indoor.id) {
                        message.add("indoor")
                    } else if (inOutRadioGroup.checkedRadioButtonId == radio_outdoor.id) {
                        message.add("outdoor")
                    }

                    val intent = Intent(this, NavigationActivity::class.java).apply {
                        putExtra(EXTRA_NAV_ARRAY, message)
                    }
                    startActivity(intent)
                }
            }
            true

        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

    /** Selectable star button allows the user to favourite locations and un-favourite*/
    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked

            /** Check which radio button was clicked, and format appropriately. */
            when (view.getId()) {
                R.id.radio_indoor ->
                    if (checked) {
                        radio_indoor.setBackgroundResource(R.color.colorOrange)
                        radio_outdoor.setBackgroundResource(R.color.colorWhite)

                    }
                R.id.radio_outdoor ->
                    if (checked) {
                        radio_outdoor.setBackgroundResource(R.color.colorOrange)
                        radio_indoor.setBackgroundResource(R.color.colorWhite)
                    }
                R.id.radio_current_location ->
                    if (checked) {
                        radio_current_location.setBackgroundResource(R.color.colorOrange)
                        radio_search_location.setBackgroundResource(R.color.colorWhite)

                    }
                R.id.radio_search_location ->
                    if (checked) {
                        radio_search_location.setBackgroundResource(R.color.colorOrange)
                        radio_current_location.setBackgroundResource(R.color.colorWhite)

                    }
            }
        }
    }
}
