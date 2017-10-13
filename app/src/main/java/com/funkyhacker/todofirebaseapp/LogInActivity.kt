package com.funkyhacker.todofirebaseapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class LogInActivity : AppCompatActivity() {

    companion object {
        fun newIntent(context: Context): Intent {
           return Intent(context, LogInActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }
}
