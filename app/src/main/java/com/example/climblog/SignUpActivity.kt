package com.example.climblog

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.android.synthetic.main.activity_navigation.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.lang.Exception

const val EXTRA_SIGNUP_EMAIL = "com.example.climblog.SIGNUP_EMAIL"
var fbAuth = FirebaseAuth.getInstance()

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        setSupportActionBar(findViewById(R.id.my_toolbar))
        supportActionBar?.title = resources.getString(R.string.button_sign_up)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    /** Inflate menu_actionbar for the action bar. */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_actionbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_done -> {
            val email = signupEmail.text.toString()
            val password = signupPassword.text.toString()
            val name = signupFirstname.text.toString() + " " + signupLastname.text.toString()

            if (email != "" && password != "") {
                try {

                    fbAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task: Task<AuthResult> ->
                            if (task.isSuccessful) {
                                //Registration OK
                                val user = fbAuth.currentUser
                                val profileUpdates = UserProfileChangeRequest.Builder()
                                    .setDisplayName(name)
                                    .build()

                                user?.updateProfile(profileUpdates)
                                    ?.addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            showMessage("Registration successful")

                                            val intent = Intent(this, MainActivity::class.java).apply {
                                                putExtra(EXTRA_SIGNUP_EMAIL, email)
                                            }
                                            startActivity(intent)
                                        }
                                    }

                            } else {
                                //Registration error
                                showMessage("Registration unsuccessful")
                            }
                        }
                } catch (e: Exception) {
                    showMessage("Registration unsuccessful")
                }


            }

            true
        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(
            this, message,
            Toast.LENGTH_SHORT
        ).show()
    }
}
