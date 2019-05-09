package com.example.climblog

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_history.view.*

/**
 * @desc The main/initial view/page for the application. Login page.
 * @author Jonah Robinson <jonahtomrobinson@gmail.com>
 * @date 07/05/2019
 */

class MainActivity : AppCompatActivity() {

    /** Grab firebase instance. */
    private var fbAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /** If email is passed from signup, populate the input field with it.*/
        val message = intent.getStringExtra("signupEmail")
        if (message != null)
            input_email.textView.apply {
                text = message
            }

        /** Refresh the session "state". */
        val preferences = applicationContext.getSharedPreferences("com.example.app.STATE", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("state", "")
        editor.apply()
    }

    /** Called when the user taps the SignUp button. */
    fun signUp(view: View) {
        startActivity(Intent(this, SignUpActivity::class.java))
    }

    /** Called when the user taps the Login button. */
    fun loginClick(view: View) {
        if (input_email.text.toString() != "" && input_password.text.toString() != "") {
            firebaseLogin(view, input_email.text.toString(), input_password.text.toString())
        }
    }

    /** Called when the user taps the "Continue without account" button. */
    fun noAccountClick(view: View) {
        if (fbAuth.currentUser != null) {
            fbAuth.signOut()
        }
        startActivity(Intent(view.context, NavigationActivity::class.java))
    }

    /**
     * Firebase login procedure.
     * Test account: test@email.com , password123 */
    private fun firebaseLogin(view: View, email: String, password: String) {
        showMessage("Authenticating...")
        fbAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, OnCompleteListener<AuthResult> { task ->

                if (task.isSuccessful) {
                    val intent = Intent(view.context, NavigationActivity::class.java)
                    intent.putExtra("id", fbAuth.currentUser?.email)

                    startActivity(intent)

                } else {
                    showMessage("Authentication failed.")
                }
            })

    }

    /** Helper function for displaying toast popups. */
    private fun showMessage(message: String) {
        Toast.makeText(
            this, message,
            Toast.LENGTH_SHORT
        ).show()
    }

}
