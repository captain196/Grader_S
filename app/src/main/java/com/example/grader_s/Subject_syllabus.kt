package com.example.grader_s

import android.annotation.SuppressLint
import android.content.Context
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

class Subject_syllabus : AppCompatActivity() {

    private lateinit var activity: String
    private lateinit var page_head: TextView
    private lateinit var toolbar: Toolbar
    private lateinit var r1: RecyclerView
    private var School_name: String = ""
    private var Subject: String = ""
    private var Class: String = ""
    private lateinit var date1: Date
    private lateinit var calendar: Calendar
    private lateinit var todaydate: String
    private lateinit var sdf: SimpleDateFormat
    private lateinit var monthName: String
    private lateinit var onBackPressedCallback :OnBackPressedCallback

    private var a1: adapter_notification = adapter_notification(listOf())
    private lateinit var Page_name: TextView
    private var list_notification: MutableList<data_class_notification> = mutableListOf()
    private var notification_list: ArrayList<Pair<String, String>> = ArrayList()

    private lateinit var databaseReference: DatabaseReference
    private lateinit var database_internal: SharedPreferences

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subject_syllabus)

        databaseReference = Firebase.database.getReference("")
        database_internal = getSharedPreferences("Database_local", MODE_PRIVATE)



        Intialize()
        Log.i("Subject",Subject)
        back_button()
        fetch_data()

    }

    private fun fetch_data() {

        databaseReference.child("Schools").child(School_name).child(Class).child("Notification").child(Subject).child(monthName).orderByChild("Time_Stamp").addListenerForSingleValueEvent(object :
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

                            val drawableName = Subject.decapitalize() + "_1"
                             val resourceId = getDrawableResourceId(this@Subject_syllabus, drawableName)

                            notification_list.add(Pair(head, content))
                            list_notification.add(data_class_notification(head, content, resourceId,date2))

                        }
                    }
                }
                Collections.reverse(notification_list)
                Collections.reverse(list_notification)
                val a1 = adapter_notification(list_notification)
                r1.layoutManager = LinearLayoutManager(
                    this@Subject_syllabus,
                    LinearLayoutManager.VERTICAL,
                    false
                )
                r1.adapter = a1

                a1.setOnItemClickListener { position ->
                    Log.i("Hindi", "Clicked")

                    val intent =
                        Intent(this@Subject_syllabus, Subject_syllabus_content::class.java)
                    intent.putExtra("Subject", Subject)
                    intent.putExtra("Position", position)
                    intent.putExtra("Subject_notification_list", notification_list)
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

        activity = intent.getStringExtra("activity").toString()
        toolbar = findViewById<Toolbar>(R.id.toolbar)
        r1 = findViewById<RecyclerView>(R.id.r1)
        Page_name = findViewById(R.id.page_name)

        School_name = database_internal.getString("School_name", "1").toString()
        Class = "Class "+database_internal.getString("Class1", "1").toString()
        Subject = intent.getStringExtra("Subject").toString()
        Page_name.setText(Subject)


        date1 = Date()
        sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -1)

        val todayCalendar = Calendar.getInstance()
        val todayYear = todayCalendar.get(Calendar.YEAR)
        val todayMonth = todayCalendar.get(Calendar.MONTH)

        monthName = getMonthName2(todayMonth)+" "+todayYear
        todaydate = sdf.format(date1)

    }

    fun getDrawableResourceId(context: Context, drawableName: String): Int {
        return context.resources.getIdentifier(drawableName, "drawable", context.packageName)
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

    private fun back_button() {
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                startActivity(Intent(this@Subject_syllabus, dashboard::class.java))
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