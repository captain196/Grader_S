package com.example.grader_s

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.Toolbar

class Profile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)


        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        toolbar.setNavigationOnClickListener {
            startActivity(Intent(this, dashboard::class.java))
            overridePendingTransition(R.anim.fade_in,0)// or perform any other action
        }

        val ed = findViewById<TextView>(R.id.edit_text_name)

        ed.setOnClickListener(){
            startActivity(Intent(this, Edit_profile::class.java))
            overridePendingTransition(R.anim.fade_in,0)// or perform any other action

        }

    }
}