package com.example.climblog

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_navigation.*


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
        if (message != null){
            FileHelper.addData(Location( message[1], message[2], message[3], message[4].toBoolean(), message[5]),"location",FileHelper.getLocationFilePath(applicationContext))
            intent.removeExtra(EXTRA_NAV_ARRAY)
            Toast.makeText(this.applicationContext, "New location added.", Toast.LENGTH_SHORT).show()
        }

        /** Initialise navigation bar */
        pageAdapter.addFragment(IndoorFragment.newInstance(),resources.getString(R.string.title_indoor))
        pageAdapter.addFragment(OutdoorFragment.newInstance(),resources.getString(R.string.title_outdoor))
        pageAdapter.addFragment(HistoryFragment.newInstance(),resources.getString(R.string.title_history))
        pageAdapter.addFragment(ProfileFragment.newInstance(),resources.getString(R.string.title_profile))
        view_pager.adapter = pageAdapter
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        findViewById<BottomNavigationView>(R.id.navigation).selectedItemId = R.id.navigation_indoor

    }
}