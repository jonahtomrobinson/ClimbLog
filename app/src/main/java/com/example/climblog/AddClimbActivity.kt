package com.example.climblog

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
import java.time.Instant
import java.util.*


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

        var test = "start"
        if(intent.getStringExtra("locationName") != null){
            test = intent.getStringExtra("locationName")
        }
        Log.d("debug1", test)
        //val helper: LinearSnapHelper
        //helper.attachToRecyclerView(HSV_difficulties2)
    }

    /** Inflate menu_actionbar for the action bar.*/
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_actionbar, menu)
        Log.d("debug1","ONCREATE")
        return true
    }

    /** On confirm creation of a new location.*/
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_done -> {
            Log.d("debug1","ONSLECT")

            val locationName = intent.getStringExtra("locationName")
            if (input_identifier.toString() != "") {

                Log.d("debug1","Before")

                var numRoutes = 0
                if (input_number_picker.value != 0){
                   numRoutes = input_number_picker.value
                }

                FileHelper.addData(
                    Set(
                        FileHelper.nextId(FileHelper.getSetFilePath(applicationContext),"set"),
                        locationName,
                        input_climb_difficulty.text.toString(),
                        selectedColor.toString(),
                        input_identifier.text.toString(),
                        Date.from(Instant.EPOCH).toString(),
                        numRoutes
                    ), "set", FileHelper.getSetFilePath(applicationContext)
                )
                Log.d("debug1","During")
                val intent = Intent(this, LocationDetailsActivity::class.java).apply {
                    putExtra("locationName", locationName)
                }
                Log.d("debug1","After")
                startActivity(intent)
                //finish()
            }

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

            // Check which radio button was clicked
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
}
