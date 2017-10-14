package com.funkyhacker.todofirebaseapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class LogInActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var signUpTextView: TextView
    private lateinit var firebaseAuth: FirebaseAuth

    companion object {
        fun newIntent(context: Context): Intent {
           return Intent(context, LogInActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        firebaseAuth = FirebaseAuth.getInstance()

        signUpTextView = findViewById(R.id.signup_text)
        emailEditText = findViewById(R.id.emailField)
        passwordEditText = findViewById(R.id.passwordField)
        loginButton = findViewById(R.id.loginButton)

        signUpTextView.setOnClickListener{ startActivity(SignUpActivity.newIntent(this)) }
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                showErrorDialog(getString(R.string.login_error_message))
            } else {
                executeAuthentication(email, password)
            }
        }
    }

    private fun executeAuthentication(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, { task: Task<AuthResult> ->
                    if (task.isSuccessful) {
                        this.startActivity(MainActivity.newIntent(this)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        )
                    } else {
                        val name: String = task.exception?.message ?: ""
                        showErrorDialog(name)
                    }
                })
    }

    private fun showErrorDialog(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.login_error_title))
                .setMessage(message)
                .setPositiveButton(getString(android.R.string.ok), null)
        val dialog = builder.create()
        dialog.show()
    }
}
