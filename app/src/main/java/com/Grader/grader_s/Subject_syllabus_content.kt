package com.Grader.grader_s

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.Toolbar
import com.example.grader_s.R

class Subject_syllabus_content : AppCompatActivity() {

    private lateinit var Subject:String
    private lateinit var toolbar:Toolbar
    private lateinit var l_arrow: ImageView
    private lateinit var r_arrow: ImageView
    private var position: Int =0
    private lateinit var Page_name: TextView
    private lateinit var head_title: TextView
    private lateinit var content: TextView
    private lateinit var onBackPressedCallback :OnBackPressedCallback

    private lateinit var data_list:  ArrayList<Pair<String, String>>

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subject_syllabus_content)

        Intialize()
        back_button()

        if(position == 0){
            l_arrow.visibility = View.GONE
        }

        if(position==(data_list.size-1)){
            r_arrow.visibility = View.GONE
        }


        l_arrow.setOnClickListener{
            position -= 1
            if (position==0) {
                l_arrow.visibility = View.GONE
                r_arrow.visibility = View.VISIBLE
            }else{
                l_arrow.visibility = View.VISIBLE
                r_arrow.visibility = View.VISIBLE
            }
            head_title.setText(data_list[position].first)
            content.setText(data_list[position].second)
        }

        r_arrow.setOnClickListener{
            position += 1
            if(position==(data_list.size-1)){
                r_arrow.visibility =View.GONE
                l_arrow.visibility = View.VISIBLE
            }else{
                r_arrow.visibility = View.VISIBLE
                l_arrow.visibility = View.VISIBLE
            }
            head_title.setText(data_list[position].first)
            content.setText(data_list[position].second)
        }
    }

    private fun Intialize() {

        toolbar = findViewById<Toolbar>(R.id.toolbar)
        l_arrow = findViewById<ImageView>(R.id.left_arrow)
        r_arrow = findViewById<ImageView>(R.id.right_arrow)
        head_title = findViewById(R.id.header_title)
        content = findViewById(R.id.content)
        Page_name = findViewById(R.id.page_name)
        position = intent.getIntExtra("Position",1)
        Subject = intent.getStringExtra("Subject").toString()
        data_list  = intent.getSerializableExtra("Subject_notification_list") as ArrayList<Pair<String, String>>

        Log.i("Data", data_list.toString())
        head_title.setText(data_list[position].first)
        content.setText(data_list[position].second)
        Page_name.setText(Subject)


    }


    private fun back_button() {
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(this@Subject_syllabus_content, Subject_syllabus::class.java)
                intent.putExtra("Subject", Subject)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right)// or perform any other action
                finish()
            }
        }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        toolbar.setNavigationOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }
    }

}