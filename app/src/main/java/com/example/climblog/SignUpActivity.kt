package com.example.climblog

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.EditText
import android.widget.FrameLayout

const val EXTRA_SIGNUP_EMAIL = "com.example.climblog.SIGNUP_EMAIL"

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
            val editText = findViewById<EditText>(R.id.signupEmail)
            val message = editText.text.toString()
                val intent = Intent(this, MainActivity::class.java).apply {
                    putExtra(EXTRA_SIGNUP_EMAIL, message)
                }
                startActivity(intent)
                //finish()
                true
            }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }
}
