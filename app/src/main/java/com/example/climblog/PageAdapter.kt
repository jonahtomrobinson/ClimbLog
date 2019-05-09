package com.example.climblog

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 * @desc Basic page adapter class, handles changing between fragments on the navigation page.
 * @author Jonah Robinson <jonahtomrobinson@gmail.com>
 * @date 07/05/2019
 */

class PageAdapter internal constructor(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    private val mFragmentList = ArrayList<Fragment>()
    private val mFragmentTitleList = ArrayList<String>()

    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }

    fun addFragment(fragment: Fragment, title: String) {
        mFragmentList.add(fragment)
        mFragmentTitleList.add(title)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mFragmentTitleList[position]
    }

    fun getPosition(title: CharSequence?): Int {
        return mFragmentTitleList.indexOf(title)
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }
}