package com.example.grader_s

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class Attendence : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attendence)


        val toolbar = findViewById<Toolbar>(R.id.toolbar_1)

        toolbar.setNavigationOnClickListener {
            // Handle the click event here
            startActivity(Intent(this, dashboard::class.java))
            overridePendingTransition(R.anim.fade_in,0)// or perform any other action
        }

    }


}