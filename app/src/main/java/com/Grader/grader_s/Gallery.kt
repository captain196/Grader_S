package com.Grader.grader_s

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.grader_s.R
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.ortiz.touchview.TouchImageView

class Gallery : AppCompatActivity() {

    private lateinit var activity: String
    private lateinit var Page_name: TextView
    private var School_name: String = ""
    private var Class: String = ""
    private lateinit var onBackPressedCallback :OnBackPressedCallback


    private lateinit var databaseReference: DatabaseReference
    private lateinit var database_internal: SharedPreferences
    private lateinit var toolbar: Toolbar
    private lateinit var r1: RecyclerView
    private lateinit var a1: adapter_gallery
    private var image_list: MutableList<dataclass_gallery> = mutableListOf()
    private var image_url_list: ArrayList<String> = ArrayList()


    private lateinit var Syllabus_image: TouchImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        database_internal = getSharedPreferences("Database_local", MODE_PRIVATE)
        databaseReference = Firebase.database.getReference()

        Intialize()
        back_button()
        fetch()


    }

    private fun fetch() {

        databaseReference.child("Schools").child(School_name).child("Gallery").orderByChild("Time_stamp").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (childSnapshot in dataSnapshot.children) {

                    val value = childSnapshot.child("image").value.toString()
                    Log.i("link", value)
                    image_list.add(dataclass_gallery(value))
                    image_url_list.add(value)
                }

                a1 = adapter_gallery(image_list)
                r1.layoutManager = GridLayoutManager(this@Gallery,4)
                r1.adapter = a1

                a1.setOnItemClickListener { position ->

                    val intent = Intent(this@Gallery, gallery_2::class.java)
                    intent.putExtra("Position",position)
                    intent.putExtra("Activity",activity)
                    intent.putExtra("image_list",image_url_list)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

                }

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
        r1 = findViewById(R.id.r1_gallery)

        School_name = database_internal.getString("School_name", "1").toString()

    }

    private fun back_button() {
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                startActivity(Intent(this@Gallery, dashboard::class.java))
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