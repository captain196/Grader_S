package com.Grader.grader_s

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.grader_s.R
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Collections
import java.util.Date
import java.util.Locale

class Notification : AppCompatActivity() {

    private lateinit var activity: String
    private lateinit var page_head: TextView
    private lateinit var toolbar: Toolbar
    private lateinit var r1: RecyclerView
    private var School_name: String = ""
    private var Subject: String = ""
    private var Class: String = ""
    private var User_name: String = ""
    private var User_Id: String = ""
    private lateinit var date1: Date
    private lateinit var calendar: Calendar
    private lateinit var todaydate: String
    private lateinit var sdf: SimpleDateFormat
    private lateinit var monthName: String
    private var a1: adapter_notification = adapter_notification(listOf())
    private lateinit var Page_name: TextView
    private lateinit var onBackPressedCallback :OnBackPressedCallback

    private var list_notification: MutableList<data_class_notification> = mutableListOf()
    private var notification_list: ArrayList<Pair<String, String>> = ArrayList()

    private lateinit var databaseReference: DatabaseReference
    private lateinit var database_internal: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        databaseReference = Firebase.database.getReference("")
        database_internal = getSharedPreferences("Database_local", MODE_PRIVATE)

        Intialize()
        back_button()
        fetch_data()


//        val i1 = listOf(
//            data_class_assignment("General", "Today our students of class X are answering their Social Science paper.", R.drawable.evaluation),
//            data_class_assignment("Geography","Today our students of class X are answering their Social Science paper." , R.drawable.logo_sc),
//            data_class_assignment("Evaluation","Today our students of class X are answering their Social Science paper.", R.drawable.evaluation),
//            data_class_assignment("Book", "Today our students of class X are answering their Social Science paper.", R.drawable.book),
//            data_class_assignment("Geography","Today our students of class X are answering their Social Science paper.", R.drawable.logo_sc),
//            data_class_assignment("Evaluation","Today our students of class X are answering their Social Science paper.", R.drawable.evaluation),
//            data_class_assignment("Geography","Today our students of class X are answering their Social Science paper.", R.drawable.logo_sc),
//            data_class_assignment("Evaluation","Today our students of class X are answering their Social Science paper.", R.drawable.logo_sc),
//            // Add more items as needed
//        )
//
//        val a1 = adapter_notification(i1)
//        r1.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
//        r1.adapter = a1
//
//        a1.setOnItemClickListener { position ->
//
//            val intent = Intent(this, Notification_content::class.java)
//            intent.putExtra("EXTRA_DATA", position.toString())
//            startActivity(intent)
//            overridePendingTransition(R.anim.fade_in,0)
//
//
//        }
    }

    private fun fetch_data() {

        databaseReference.child("Schools").child(School_name).child(Class).child("Students").child(User_Id).child("Notification").child(monthName).orderByChild("Time_Stamp").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (childSnapshot in dataSnapshot.children) {
                    var date2 =childSnapshot.key.toString()
                    for (childSnapshot2 in childSnapshot.children) {
                        val head = childSnapshot2.key.toString()
                        for (childSnapshot3 in childSnapshot2.children) {
                            val content = childSnapshot3.value.toString()

                            val todayDate: String = sdf.format(date1)
                            val yesterdayDate = sdf.format(calendar.time)

                            if (todayDate == date2) {
                                date2 = "Today"
                            } else if (yesterdayDate == date2) {
                                date2 = "Yesterday"
                            }
                            notification_list.add(Pair(head, content))
                            list_notification.add(
                                data_class_notification(head, content, R.drawable.logo, date2)
                            )

                        }
                    }
                }

//                for (childSnapshot in dataSnapshot.children) {
//
//                    val key = childSnapshot.key.toString()
//                    val value = childSnapshot.child("value").value.toString()
//
//                    notification_list.add(Pair(key,value))
//                    list_notification.add(data_class_notification(key,value,R.drawable.logo))
//
//                }

                Collections.reverse(notification_list)
                Collections.reverse(list_notification)
                val a1 = adapter_notification(list_notification)
                r1.layoutManager = LinearLayoutManager(this@Notification, LinearLayoutManager.VERTICAL, false)
                r1.adapter = a1

                a1.setOnItemClickListener { position ->

                    val intent = Intent(this@Notification, Notification_content::class.java)
                    intent.putExtra("Position",position)
                    intent.putExtra("Notification_list",notification_list)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
                println("Error fetching data: ${databaseError.message}")
            }

        })
    }

    private fun Intialize() {

        activity = intent.getStringExtra("Activity").toString()
        toolbar = findViewById<Toolbar>(R.id.toolbar)
        r1 = findViewById<RecyclerView>(R.id.r1)
        Page_name = findViewById(R.id.page_name)

        User_Id = database_internal.getString("User_Id", "1").toString()
        User_name = database_internal.getString("User_name", "Unknown").toString()
        School_name = database_internal.getString("School_name", "1").toString()
        Class = "Class "+database_internal.getString("Class1", "1").toString()
        Page_name.setText(activity)

        date1 = Date()
        sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -1)

        val todayCalendar = Calendar.getInstance()
        val todayYear = todayCalendar.get(Calendar.YEAR)
        val todayMonth = todayCalendar.get(Calendar.MONTH)

        monthName = getMonthName2(todayMonth)+" "+todayYear
        todaydate = sdf.format(date1)

        Log.i("notifi",User_Id)
        Log.i("notifi2",User_name)

    }

    private fun back_button() {
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                startActivity(Intent(this@Notification, dashboard::class.java))
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right)// or perform any other action
                finish()
            }
        }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        toolbar.setNavigationOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }
    }

    private fun getMonthName2(month: Int): String {
        return when (month) {
            Calendar.JANUARY -> "January"
            Calendar.FEBRUARY -> "February"
            Calendar.MARCH -> "March"
            Calendar.APRIL -> "April"
            Calendar.MAY -> "May"
            Calendar.JUNE -> "June"
            Calendar.JULY -> "July"
            Calendar.AUGUST -> "August"
            Calendar.SEPTEMBER -> "September"
            Calendar.OCTOBER -> "October"
            Calendar.NOVEMBER -> "November"
            Calendar.DECEMBER -> "December"
            else -> "Invalid month"
        }
    }
}