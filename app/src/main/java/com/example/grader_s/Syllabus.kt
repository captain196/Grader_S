package com.example.grader_s

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import com.ortiz.touchview.TouchImageView
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File
import java.net.URLEncoder


class Syllabus : AppCompatActivity() {

    private lateinit var activity: String
    private lateinit var Page_name: TextView
    private var School_name: String = ""
    private var Class: String = ""

    private lateinit var databaseReference: DatabaseReference
    private lateinit var database_internal: SharedPreferences
    private lateinit var toolbar: Toolbar
    private lateinit var Syllabus_image: TouchImageView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_syllabus)

        databaseReference = Firebase.database.getReference()
        database_internal = getSharedPreferences("Database_local", MODE_PRIVATE)

        Intialize()
//        fetch_image()
        back_button()
    }

    private fun fetch_image(){

        databaseReference.child("Schools").child(School_name).child("Holidays").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val value = dataSnapshot.value.toString()
                Log.d("MainActivity", "Value is: $value")
                Picasso.get().load(value).into(Syllabus_image)
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("Message", "Failed to read Image", error.toException())
            }
        })
    }

    private fun back_button() {

        toolbar.setNavigationOnClickListener {
            startActivity(Intent(this, dashboard::class.java))
            overridePendingTransition(R.anim.fade_in,0)// or perform any other action
        }
    }
    private fun Intialize() {

        activity = intent.getStringExtra("Activity").toString()
        toolbar = findViewById<Toolbar>(R.id.toolbar)
        Page_name = findViewById(R.id.page_name)
        Page_name.setText(activity)

        Syllabus_image = findViewById<TouchImageView>(R.id.syllabus_image)
        School_name = database_internal.getString("School_name", "1").toString()
        Class = "Class "+database_internal.getString("Class1", "1").toString()

    }
}