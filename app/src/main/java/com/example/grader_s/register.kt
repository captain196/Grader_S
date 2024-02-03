package com.example.grader_s

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.material.textview.MaterialTextView

class register : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val login1 = findViewById<MaterialTextView>(R.id.login_text)
        login1.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            overridePendingTransition(R.anim.fade_in,0)
            finish()
        })
    }
}