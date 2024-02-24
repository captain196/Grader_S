package com.example.grader_s

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar

class Subject_syllabus_content : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subject_syllabus_content)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        toolbar.setNavigationOnClickListener {
            startActivity(Intent(this, Subject_syllabus::class.java))
            overridePendingTransition(R.anim.fade_in,0)// or perform any other action
        }

        val l_arrow = findViewById<ImageView>(R.id.left_arrow)
        val r_arrow = findViewById<ImageView>(R.id.right_arrow)

        val receivedData = intent.getStringExtra("EXTRA_DATA")
        if (receivedData != null) {
            Log.i("Message",receivedData)

            var position = receivedData.toInt()
            l_arrow.setOnClickListener{
                position -= 1
                Log.i("Message",position.toString())
            }

            r_arrow.setOnClickListener{
                position += 1
                Log.i("Message",position.toString())
            }

        }
    }
}