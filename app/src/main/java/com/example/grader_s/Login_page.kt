package com.example.grader_s

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textview.MaterialTextView


class Login_page : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)

        //animation()

        val register1 = findViewById<MaterialTextView>(R.id.register)

        register1.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, register::class.java))
            overridePendingTransition(R.anim.fade_in,0)
            finish()
        })

        val login = findViewById<Button>(R.id.loginButton)

        login.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, dashboard::class.java))
            overridePendingTransition(R.anim.fade_in,0)
            finish()
        })


    }

    fun animation(){

        val displayMetrics = resources.displayMetrics
        val screenHeightDp = displayMetrics.heightPixels / displayMetrics.density


        val logo = findViewById<View>(R.id.logo)
        logo.animate().translationYBy(((screenHeightDp*(-3))/4))
            .setDuration(1000)
            .start()
    }
}