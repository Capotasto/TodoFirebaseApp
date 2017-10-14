package com.funkyhacker.todofirebaseapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseUser: FirebaseUser

    private lateinit var database: DatabaseReference
    private lateinit var userId: String

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firebaseAuth = FirebaseAuth.getInstance()
        if (firebaseAuth.currentUser != null) {
            firebaseUser = firebaseAuth.currentUser!!
        } else {
            loadLogInView()
        }

        database = FirebaseDatabase.getInstance().reference
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item != null) {
            if (item.itemId == R.id.action_logout) {
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }


    private fun loadLogInView() {
        startActivity(LogInActivity.newIntent(this)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        )
    }
}
