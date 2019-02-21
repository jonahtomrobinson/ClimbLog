package com.example.climblog

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

class PagerAdapter(fragmentManager: FragmentManager): FragmentStatePagerAdapter(fragmentManager) {

    var mNumOfTabs: Int = 0


    override fun getItem(position: Int): Fragment {
        return BlankFragment.newInstance()
    }

    override  fun getCount(): Int {
        return 1
    }

}