package com.example.climblog

import android.app.Application
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.NumberPicker
import android.widget.RadioButton
import kotlinx.android.synthetic.main.activity_add_climb.*
import kotlinx.android.synthetic.main.activity_add_location.*
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.support.v7.widget.LinearSnapHelper
import android.support.v7.widget.SnapHelper
import android.util.Log
import android.widget.Toast
import com.flask.colorpicker.builder.ColorPickerClickListener
import com.flask.colorpicker.OnColorSelectedListener
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerDialogBuilder
import kotlinx.android.synthetic.main.activity_navigation.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.time.Instant
import java.time.LocalDate
import java.util.*

/**
 * @desc Form for adding new climbs (routes or sets).
 * @author Jonah Robinson <jonahtomrobinson@gmail.com>
 * @date 07/05/2019
 */

class AddClimbActivity : AppCompatActivity() {

    var selectedColor = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_climb)

        /** Set the custom actionbar and title.*/
        setSupportActionBar(findViewById(R.id.my_toolbar))
        supportActionBar?.title = "Add Climb"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        /** Setting values for the number picker.*/
        val np = input_number_picker
        np.minValue = 0
        np.maxValue = 100
        np.wrapSelectorWheel = false
    }

    /** Inflate menu_actionbar for the action bar.*/
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_actionbar, menu)
        Log.d("debug1", "ONCREATE")
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {

        /** Upon click of the action_done button, add a new set.*/
        R.id.action_done -> {

            val difficulty = input_climb_difficulty.text.toString()

            /** Validate inputs */
            if (selectedColor != 0 && difficulty != "" &&
                (input_number_picker.value != 0 || input_identifier.text.toString() != "")
            ) {
                val locationName = intent.getStringExtra("locationName")
                var numRoutes = 0
                if (input_number_picker.value != 0) {
                    numRoutes = input_number_picker.value
                }

                /** Adding a new set. */
                FileHelper.addData(
                    Set(
                        FileHelper.nextId("set", FileHelper.getSetFilePath(applicationContext)),
                        locationName,
                        input_climb_difficulty.text.toString().toUpperCase(),
                        selectedColor.toString(),
                        input_identifier.text.toString(),
                        LocalDate.now().toString(),
                        numRoutes
                    ), "set", FileHelper.getSetFilePath(applicationContext)
                )
                val intent = Intent(this, LocationDetailsActivity::class.java).apply {
                    putExtra("locationName", locationName)
                    showMessage("New climb added")
                }
                startActivity(intent)
            } else {
                showMessage("Please complete all inputs")
            }
            true
        }

        /** Upon click of the home button, return to the previous view/page. */
        android.R.id.home -> {
            val intent = Intent(this, LocationDetailsActivity::class.java).apply {
                putExtra("locationName", intent.getStringExtra("locationName"))
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

    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked

            /** Check which radio button was clicked, and format appropriately. */
            when (view.getId()) {
                R.id.radio_route ->
                    if (checked) {
                        radio_route.setBackgroundResource(R.color.colorOrange)
                        radio_set.setBackgroundResource(R.color.colorWhite)
                        input_identifier.visibility = View.VISIBLE
                        input_number_picker.visibility = View.GONE
                        tv_number_picker.visibility = View.GONE
                        input_number_picker.value = 0
                    }
                R.id.radio_set ->
                    if (checked) {
                        radio_set.setBackgroundResource(R.color.colorOrange)
                        radio_route.setBackgroundResource(R.color.colorWhite)
                        input_identifier.visibility = View.GONE
                        input_identifier.text.clear()
                        input_number_picker.visibility = View.VISIBLE
                        tv_number_picker.visibility = View.VISIBLE

                    }
            }
        }
    }

    /** Colour picker setup. */
    fun onButtonClick(view: View) {
        ColorPickerDialogBuilder
            .with(view.context)
            .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
            .setTitle("Choose color")
            .initialColor(Color.WHITE)
            .showLightnessSlider(true)
            .showAlphaSlider(false)
            .density(12)
            .setOnColorSelectedListener {
                selectedColor = it
            }
            .setPositiveButton("ok", ColorPickerClickListener(
                fun(dialogBuilder: DialogInterface, selectedColor: Int, allColors: Array<Int>) {
                    findViewById<View>(R.id.my_toolbar).setBackgroundColor(selectedColor)
                }
            ))
            .build()
            .show()

    }

    /** Helper function for displaying toast popups. */
    private fun showMessage(message: String) {
        Toast.makeText(
            this, message,
            Toast.LENGTH_SHORT
        ).show()
    }

}
