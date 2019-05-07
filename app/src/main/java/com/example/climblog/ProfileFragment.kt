package com.example.climblog

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.opengl.Visibility
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment : Fragment() {

    val fbAuth = FirebaseAuth.getInstance()

    companion object {
        fun newInstance(): ProfileFragment {
            return ProfileFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (fbAuth.currentUser?.displayName != null){
            tv_profile_name.text = fbAuth.currentUser?.displayName.toString()
            tv_profile_name.visibility = View.VISIBLE
            tv_profile.visibility = View.GONE
            btn_login_link.visibility = View.GONE
        }
        else{
            tv_profile.visibility = View.VISIBLE
            btn_login_link.visibility = View.VISIBLE
            tv_profile_name.visibility = View.GONE
        }

        btn_login_link.setOnClickListener {
            startActivity(Intent(activity, MainActivity::class.java))
        }
    }
}
