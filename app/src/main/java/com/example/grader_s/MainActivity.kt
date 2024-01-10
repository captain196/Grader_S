package com.example.grader_s

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        animation()
    }

    fun animation(){
        val logo = findViewById<View>(R.id.logo)
        val zoomInAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_up)

        logo.startAnimation(zoomInAnimation)
    }
}