package com.Grader.grader_s

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import com.example.grader_s.R
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import coil.load
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.ortiz.touchview.TouchImageView

class Time_table : AppCompatActivity() {

    private lateinit var activity: String
    private lateinit var Page_name: TextView
    private var School_name: String = ""
    private var Class: String = ""
    private lateinit var Image_link: String
    private lateinit var onBackPressedCallback :OnBackPressedCallback


    private lateinit var databaseReference: DatabaseReference
    private lateinit var database_internal: SharedPreferences
    private lateinit var toolbar: Toolbar
    private lateinit var Syllabus_image: TouchImageView
    private lateinit var progress_bar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_table)

        database_internal = getSharedPreferences("Database_local", MODE_PRIVATE)
        databaseReference = Firebase.database.getReference()

        Intialize()
        progress_bar.visibility = View.VISIBLE
        fetch_image()
        back_button()

    }

    private fun fetch_image(){

        databaseReference.child("Schools").child(School_name).child(Class).child("Time_table").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val value = dataSnapshot.value.toString()
                Log.i("link", value)

                Syllabus_image.load(value) {
                    placeholder(R.drawable.transparent)
                    error(R.drawable.time_table)
                    crossfade(true)
                    listener(onSuccess = { _, _ ->
                        progress_bar.visibility = View.GONE // Hide progress bar on success
                    }, onError = { _, _ ->
                        progress_bar.visibility = View.GONE // Hide progress bar on error
                    })
                    // Additional options can be added here
                }
//
//                Picasso.get().load(value)
//                    .into(object : Target {
//                        override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
//                            Syllabus_image.setImageBitmap(bitmap)
//                            progress_bar.visibility = View.GONE
//                        }
//
//                        override fun onBitmapFailed(e: java.lang.Exception?, errorDrawable: Drawable?) {
//                            // Handle error
//                            progress_bar.visibility = View.GONE
//                        }
//
//                        override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
//                            // You can optionally use this callback to show a placeholder image
//                            progress_bar.visibility = View.VISIBLE
//                        }
//                    })
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("Message", "Failed to read Image", error.toException())
            }
        })
    }

    private fun Intialize() {

        activity = intent.getStringExtra("Activity").toString()
        toolbar = findViewById<Toolbar>(R.id.toolbar)
        Page_name = findViewById(R.id.page_name)
        Page_name.setText(activity)

        progress_bar = findViewById(R.id.progress_time_table_image)
        Syllabus_image = findViewById<TouchImageView>(R.id.time_table_image)
        School_name = database_internal.getString("School_name", "1").toString()
        Class = "Class " + database_internal.getString("Class1", "1").toString()

        Log.i("Class",Class)
    }

    private fun back_button() {
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                startActivity(Intent(this@Time_table, dashboard::class.java))
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right)// or perform any other action
                finish()
            }
        }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        toolbar.setNavigationOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }
    }
}