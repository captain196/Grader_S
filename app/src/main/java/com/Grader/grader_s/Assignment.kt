package com.Grader.grader_s

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
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




class Assignment : AppCompatActivity() {

    private lateinit var activity: String
    private lateinit var r1: RecyclerView
    private lateinit var Page_name: TextView
    private var School_name: String = ""
    private var Class: String = ""
    private lateinit var date1: Date
    private lateinit var sdf: SimpleDateFormat
    private lateinit var calendar: Calendar
    private lateinit var onBackPressedCallback :OnBackPressedCallback
    private var list_assignment: MutableList<data_class_assignments> = mutableListOf()
    private var assignment_data_list: ArrayList<Triple<String,String,String>> = ArrayList()


    private lateinit var databaseReference: DatabaseReference
    private lateinit var database_internal: SharedPreferences

    private lateinit var toolbar: Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_assignment)

        databaseReference = Firebase.database.getReference("")
        database_internal = getSharedPreferences("Database_local", MODE_PRIVATE)


        Intialize()
        back_button()
        fetch_data()


    }

    private fun back_button() {

        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

                startActivity(Intent(this@Assignment, dashboard::class.java))
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right)// or perform any other action
                finish()

            }
        }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        toolbar.setNavigationOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }
    }


    private fun fetch_data() {

        databaseReference.child("Schools").child(School_name).child(Class).child("Assignments").orderByChild("Time_Stamp").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Check if dataSnapshot has children
                for (childSnapshot in dataSnapshot.children) {
                    for(childSnapshot2 in childSnapshot.children) {
                        val head = childSnapshot2.key.toString()
                        for(childSnapshot3 in childSnapshot2.children) {
                            val key = childSnapshot3.key.toString()
                            for(childSnapshot4 in childSnapshot3.children) {
                                val value = childSnapshot4.value.toString()
                                var date = childSnapshot.key.toString()
                                val todayDate: String = sdf.format(date1)
                                val yesterdayDate = sdf.format(calendar.time)

                                if (todayDate == date) {
                                    date = "Today"
                                } else if (yesterdayDate == date) {
                                    date = "Yesterday"
                                }

                                val drawableName = head.decapitalize() + "_1"
                                Log.i(
                                    "subject_name",
                                    drawableName
                                )// Replace "your_drawable_name" with the name of your drawable file without extension
                                val resourceId =
                                    getDrawableResourceId(this@Assignment, drawableName)

                                assignment_data_list.add(Triple(key, value, date))
                                list_assignment.add(
                                    data_class_assignments(
                                        key,
                                        value,
                                        resourceId,
                                        date
                                    )
                                )
                                Log.i("Data", list_assignment.toString())

                            }
                        }
                    }
                }
                Collections.reverse(list_assignment)
                Collections.reverse(assignment_data_list)
                val a1 = adapter_assignment(list_assignment)
                r1.layoutManager = LinearLayoutManager(this@Assignment, LinearLayoutManager.VERTICAL, false)
                r1.adapter = a1

//                a1.setOnItemClickListener { position ->
//
//                    val intent = Intent(this@Assignment, assignment_data::class.java)
//                    intent.putExtra("Position",position)
//                    intent.putExtra("Activity",activity)
//                    intent.putExtra("Assignment_list",assignment_data_list)
//                    startActivity(intent)
//                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
//
//                }
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

        School_name = database_internal.getString("School_name", "1").toString()
        Class = "Class "+database_internal.getString("Class1", "1").toString()
        Page_name.setText(activity)

        date1 = Date()
        sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

        calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -1)

        Log.i("School",School_name)
        Log.i("Class",Class)

    }

    fun getDrawableResourceId(context: Context, drawableName: String): Int {
        return context.resources.getIdentifier(drawableName, "drawable", context.packageName)
    }

}


