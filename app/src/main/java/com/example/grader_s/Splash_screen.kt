package com.example.grader_s

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import com.google.firebase.FirebaseApp

class Splash_screen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        // Circle Animation

        loadAnimation()


        // Moving to Login
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, Login_page::class.java))
            overridePendingTransition(R.anim.fade_in,0)
            finish()
        },3000)
    }

    fun loadAnimation(){

        val circleView = findViewById<View>(R.id.circleView)
        val logo = findViewById<View>(R.id.logo)

        // Load the animation
        val zoomInAnimation = AnimationUtils.loadAnimation(this, R.anim.zoom_in)

        // Apply the animation to the circle view
        circleView.startAnimation(zoomInAnimation)
    }
}