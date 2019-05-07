package com.example.climblog

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.NavigationView
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_navigation.*
import kotlinx.android.synthetic.main.fragment_indoor.*
import kotlinx.android.synthetic.main.fragment_outdoor.*


class NavigationActivity : AppCompatActivity() {

    private val pageAdapter = PageAdapter(supportFragmentManager)

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        message.text = item.title
        supportActionBar?.title = item.title
        view_pager.currentItem = pageAdapter.getPosition(item.title)

        return@OnNavigationItemSelectedListener true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)
        setSupportActionBar(findViewById(R.id.my_toolbar))

        /** Adding a new Location.*/
        val message = intent.getStringArrayListExtra(EXTRA_NAV_ARRAY)
        if (message != null) {
            FileHelper.addData(
                Location(
                    FileHelper.nextId(
                        FileHelper.getLocationFilePath(applicationContext),
                        "location"
                    ), message[1], message[2], message[3], message[4].toBoolean(), message[5]
                ), "location", FileHelper.getLocationFilePath(applicationContext)
            )
            intent.removeExtra(EXTRA_NAV_ARRAY)
            Toast.makeText(this.applicationContext, "New location added.", Toast.LENGTH_SHORT).show()
        }

        /** Initialise navigation bar */
        pageAdapter.addFragment(IndoorFragment.newInstance(), resources.getString(R.string.title_indoor))
        pageAdapter.addFragment(OutdoorFragment.newInstance(), resources.getString(R.string.title_outdoor))
        pageAdapter.addFragment(HistoryFragment.newInstance(), resources.getString(R.string.title_history))
        pageAdapter.addFragment(ProfileFragment.newInstance(), resources.getString(R.string.title_profile))
        view_pager.adapter = pageAdapter

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

                if (positionOffset.compareTo(0.0) == 0) {
                    /** Show all floating buttons. Both indoor and outdoor.*/
                    when (navigation.menu.getItem(position).itemId){
                        R.id.navigation_indoor -> float_add_location_in.show()
                        R.id.navigation_outdoor -> float_add_location_out.show()
                    }

                    /** Set bottom navigation item based on position.*/
                    navigation.selectedItemId = navigation.menu.getItem(position).itemId
                } else {
                    /** Hide all floating buttons. Both indoor and outdoor.*/
                    float_add_location_in.hide()
                    float_add_location_out.hide()
                }
            }
            override fun onPageSelected(position: Int) {}
            override fun onPageScrollStateChanged(state: Int) {}
        })

        checkSession()
    }

    override fun onStart() {
        super.onStart()
        checkSession()
    }

    private fun checkSession() {
        val preferences = applicationContext.getSharedPreferences("com.example.app.STATE", Context.MODE_PRIVATE)
        if (preferences.getString("state", "") != "") {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("A climbing session is currently active, would you like to end the session?")

            builder.setPositiveButton("End session") { dialog, which ->
                Toast.makeText(
                    this.applicationContext,
                    "Session ended", Toast.LENGTH_SHORT
                ).show()
                val editor = preferences.edit()
                editor.putString("state", "")
                editor.apply()
            }

            builder.setNegativeButton("No") { dialog, which -> }
            builder.create()
            builder.show()
        }
    }
}