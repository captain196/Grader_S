package com.example.grader_s

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Notification : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        toolbar.setNavigationOnClickListener {
            startActivity(Intent(this, dashboard::class.java))
            overridePendingTransition(R.anim.fade_in,0)// or perform any other action
        }


        val r1 = findViewById<RecyclerView>(R.id.r1)

        val i1 = listOf(
            data_class_assignment("General", "Today our students of class X are answering their Social Science paper.", R.drawable.evaluation),
            data_class_assignment("Geography","Today our students of class X are answering their Social Science paper." , R.drawable.logo_sc),
            data_class_assignment("Evaluation","Today our students of class X are answering their Social Science paper.", R.drawable.evaluation),
            data_class_assignment("Book", "Today our students of class X are answering their Social Science paper.", R.drawable.book),
            data_class_assignment("Geography","Today our students of class X are answering their Social Science paper.", R.drawable.logo_sc),
            data_class_assignment("Evaluation","Today our students of class X are answering their Social Science paper.", R.drawable.evaluation),
            data_class_assignment("Geography","Today our students of class X are answering their Social Science paper.", R.drawable.logo_sc),
            data_class_assignment("Evaluation","Today our students of class X are answering their Social Science paper.", R.drawable.logo_sc),
            // Add more items as needed
        )

        val a1 = adapter_notification(i1)
        r1.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        r1.adapter = a1

        a1.setOnItemClickListener { position ->

            val intent = Intent(this, Notification_content::class.java)
            intent.putExtra("EXTRA_DATA", position.toString())
            startActivity(intent)
            overridePendingTransition(R.anim.fade_in,0)


        }
    }
}