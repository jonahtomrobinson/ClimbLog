package com.example.climblog

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
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

        /** Initialise navigation bar */
        pageAdapter.addFragment(ProfileFragment.newInstance(),resources.getString(R.string.title_indoor),0)
        pageAdapter.addFragment(BlankFragment.newInstance(),resources.getString(R.string.title_outdoor),1)
        pageAdapter.addFragment(ProfileFragment.newInstance(),resources.getString(R.string.title_history),2)
        pageAdapter.addFragment(BlankFragment.newInstance(),resources.getString(R.string.title_profile),3)
        view_pager.adapter = pageAdapter
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        findViewById<BottomNavigationView>(R.id.navigation).selectedItemId = R.id.navigation_indoor

    }
}
