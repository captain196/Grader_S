package com.example.grader_s

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar

class Notification_content : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_content)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        toolbar.setNavigationOnClickListener {
            startActivity(Intent(this, Notification::class.java))
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