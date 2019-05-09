package com.example.climblog

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * @desc Fragment for the history view/page. NOT CURRENTLY FUNCTIONAL.
 * @author Jonah Robinson <jonahtomrobinson@gmail.com>
 * @date 07/05/2019
 */

class HistoryFragment : Fragment() {

    companion object {
        fun newInstance(): HistoryFragment {
            return HistoryFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

}
