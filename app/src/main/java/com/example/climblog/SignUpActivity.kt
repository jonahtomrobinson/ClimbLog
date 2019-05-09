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

/**
 * @desc A signup page, allows creation on new users through firebase.
 * @author Jonah Robinson <jonahtomrobinson@gmail.com>
 * @date 07/05/2019
 */

class SignUpActivity : AppCompatActivity() {

    /** Grab firebase instance. */
    private var fbAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        /** Set the custom actionbar title.*/
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

    /** Action bar button listeners. */
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_done -> {

            /** Grab signup details from text fields. */
            val email = signupEmail.text.toString()
            val password = signupPassword.text.toString()
            val name = signupFirstname.text.toString() + " " + signupLastname.text.toString()

            /** Attempt to add the new users, informs of success and failure as appropriate. */
            if (email != "" && password != "") {
                fbAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task: Task<AuthResult> ->
                        if (task.isSuccessful) {

                            //Registration OK
                            val user = fbAuth.currentUser
                            val profileUpdates = UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .build()

                            /** Attempt to add user's name to firebase details. */
                            user?.updateProfile(profileUpdates)
                                ?.addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        showMessage("Registration successful")
                                        val intent = Intent(this, MainActivity::class.java).apply {
                                            putExtra("signupEmail", email)
                                        }
                                        startActivity(intent)
                                    } else {
                                        showMessage("Registration unsuccessful")
                                    }
                                }

                        } else {
                            //Registration error
                            showMessage("Registration unsuccessful")
                        }
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

    /** Helper function for displaying toast popups. */
    private fun showMessage(message: String) {
        Toast.makeText(
            this, message,
            Toast.LENGTH_SHORT
        ).show()
    }
}
