package com.Grader.grader_s

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.grader_s.R


class assignment_data : AppCompatActivity() {

    private lateinit var activity:String
    private lateinit var toolbar:Toolbar
    private lateinit var l_arrow: ImageView
    private lateinit var r_arrow: ImageView
    private var position: Int =0
    private lateinit var head_title: TextView
    private lateinit var Page_name: TextView
    private lateinit var content: TextView
    private lateinit var date1: TextView
    private lateinit var subject: TextView
    private lateinit var data_list:  ArrayList<Triple<String,String,String>>
    private lateinit var onBackPressedCallback : OnBackPressedCallback



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_assignment_data)

        Intialize()
        back_button()
        controls()

    }

    private fun controls() {

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
            date1.setText(data_list[position].third)
        }

        r_arrow.setOnClickListener{
            position += 1
            if(position==(data_list.size-1)){
                r_arrow.visibility = View.GONE
                l_arrow.visibility = View.VISIBLE
            }else{
                r_arrow.visibility = View.VISIBLE
                l_arrow.visibility = View.VISIBLE
            }
            head_title.setText(data_list[position].first)
            content.setText(data_list[position].second)
            date1.setText(data_list[position].third)
        }
    }


    private fun Intialize() {

        toolbar = findViewById<Toolbar>(R.id.toolbar)
        l_arrow = findViewById<ImageView>(R.id.left_arrow1)
        r_arrow = findViewById<ImageView>(R.id.right_arrow1)
        head_title = findViewById(R.id.header_title1)
        content = findViewById(R.id.content1)
        date1 = findViewById(R.id.date1)
        Page_name = findViewById(R.id.page_name)
        //subject = findViewById(R.id.subject)
        position = intent.getIntExtra("Position",1)
        activity = intent.getStringExtra("Activity").toString()
        data_list  = intent.getSerializableExtra("Assignment_list") as ArrayList<Triple<String,String,String>>

        Log.i("Data", data_list.toString())
        head_title.setText(data_list[position].first)
        content.setText(data_list[position].second)
        date1.setText(data_list[position].third)
        Page_name.setText(activity)


    }

    private fun back_button() {
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(this@assignment_data, Assignment::class.java)
                intent.putExtra("Activity", activity)
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