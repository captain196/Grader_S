package com.example.grader_s

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar

class Edit_profile : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        toolbar.setNavigationOnClickListener {
            startActivity(Intent(this, Profile::class.java))
            overridePendingTransition(R.anim.fade_in,0)// or perform any other action
        }

    }
}