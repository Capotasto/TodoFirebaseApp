package com.funkyhacker.todofirebaseapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseUser: FirebaseUser

    private lateinit var database: DatabaseReference
    private lateinit var userId: String
    private lateinit var listView : RecyclerView
    private lateinit var adapter : MyAdapter
    private lateinit var items : ArrayList<String>

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
        if (firebaseAuth.currentUser == null) {
            loadLogInView()
        } else {
            firebaseUser = firebaseAuth.currentUser!!
            userId = firebaseUser.uid;

            listView = findViewById(R.id.listView)
            items = ArrayList()
            items.add("1")
            items.add("2")
            items.add("3")
            adapter = MyAdapter(items)
            listView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
            listView.adapter = adapter

            val editText = findViewById<EditText>(R.id.todoText)
            val button = findViewById<Button>(R.id.addButton)
            button.setOnClickListener {
                Log.d("Why", "fuck")
                database.child("users").child(userId).child("items").push().child("title").setValue(editText.text.toString())
                editText.setText("")
            }

            database.child("users").child(userId).child("items").addChildEventListener(object : ChildEventListener {
                override fun onCancelled(p0: DatabaseError?) {
                }

                override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                    if (p0 == null)  return
                    insertToRecyclerview(p0.child("title").value as String)
                }

                override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
                }

                override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
                }

                override fun onChildRemoved(p0: DataSnapshot?) {
                    if  (p0 == null) return
                    removeItem(p0.child("title").value as String)
                }
            })
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item != null) {
            if (item.itemId == R.id.action_logout) {
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun insertToRecyclerview(text: String) {
        items.add(0, text)
        adapter.notifyItemInserted(0)
    }

    private fun removeItem(text: String) {
        val index = items.indexOf(text)
        if (index >= 0) {
            items.remove(text)
            adapter.notifyItemRemoved(index)
        }
    }


    private fun loadLogInView() {
        startActivity(LogInActivity.newIntent(this)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        )
    }


}
