package com.Grader.grader_s

import CalendarAdapter
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.grader_s.R

import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import com.google.android.material.progressindicator.LinearProgressIndicator;


class Attendance : AppCompatActivity() {

    private lateinit var activity: String
    private lateinit var Page_name: TextView
    private lateinit var toolbar: Toolbar
    private lateinit var monthYearText: TextView
    private lateinit var recyclerViewCalendar: RecyclerView
    private lateinit var prevMonth: ImageView
    private lateinit var nextMonth: ImageView
    private lateinit var attendanceInfoLayout: FrameLayout
    private lateinit var attendanceInfoText: TextView
    private var currentMonth: Int = 0
    private var currentYear: Int = 0
    private lateinit var school_Id: String
    private lateinit var user_Id: String
    private  lateinit var school_name: String
    private lateinit var class1: String
    private lateinit var monthName: String
    private var Month_value: Int = 0
    private var Year_value: Int = 0
    private var current_session: Int = 0
    private lateinit var Restricted_below: String
    private lateinit var Restricted_above: String
    private lateinit var total_classes_done: TextView
    private lateinit var total_classes_left: TextView
    private lateinit var onBackPressedCallback : OnBackPressedCallback
    private lateinit var Attendance_progressbar: LinearProgressIndicator
    private lateinit var Attendance_progressbar_percent: TextView
    private lateinit var Max_progressbar: LinearProgressIndicator
    private lateinit var Min_progressbar: LinearProgressIndicator
    private lateinit var Max_progressbar_percent: TextView
    private lateinit var Min_progressbar_percent: TextView
    private var Total_present: Int = 0




    private lateinit var databaseReference: DatabaseReference
    private lateinit var databaseReference2: DatabaseReference
    private lateinit var database_internal: SharedPreferences
    data class MonthStatus(val statusString: String)
    private val monthStatus = MonthStatus("APALPPPPPPPPPPPPPPPPPPPPPPAAAA")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attendance)

        database_internal = getSharedPreferences("Database_local", MODE_PRIVATE)


        Intialize()
        databaseReference = Firebase.database.getReference().child("Schools").child(school_name).child(class1).child("Students").child(user_Id).child("Attendance")
        databaseReference2 = Firebase.database.getReference().child("Schools").child(school_name)

        fetch_details()
        progress_bar()
        back_button()


        val calendar = Calendar.getInstance()
        currentMonth = calendar.get(Calendar.MONTH)
        currentYear = calendar.get(Calendar.YEAR)

        if(currentMonth>=3) {
            Restricted_below = getMonthName2(2) + " " + currentYear.toString()
            Restricted_above = getMonthName2(3) + " " + (currentYear + 1).toString()
        }else{
            Restricted_below = getMonthName2(2) + " " + (currentYear-1).toString()
            Restricted_above = getMonthName2(3) + " " + (currentYear).toString()
        }
        Month_value = currentMonth % 12
        Year_value = currentYear

        updateMonthYearHeader(Month_value, Year_value)
        setUpRecyclerView(Month_value, Year_value)

        prevMonth.setOnClickListener {
            attendanceInfoText.visibility = View.GONE
            val month_check = getMonthName2((Month_value - 1)%12)+" "+Year_value
            if(month_check == Restricted_below){
                Toast.makeText(this, "Not Available", Toast.LENGTH_SHORT).show()
            }else {
                Month_value = (Month_value - 1) % 12
                if (Month_value < 0) {
                    Month_value = 11
                    Year_value -= 1
                }
                updateMonthYearHeader(Month_value, Year_value)
                setUpRecyclerView(Month_value, Year_value)
            }
        }

        nextMonth.setOnClickListener {
            attendanceInfoText.visibility = View.GONE
            val month_check = getMonthName2((Month_value + 1) % 12) + " " + Year_value
            if (month_check == Restricted_above) {
                Toast.makeText(this, "Not Available", Toast.LENGTH_SHORT).show()
            } else {
                Month_value = (Month_value + 1)
                if (Month_value >= 12) {
                    Year_value += 1
                    Month_value %= 12
                }
                updateMonthYearHeader(Month_value, Year_value)
                setUpRecyclerView(Month_value, Year_value)
            }
        }

    }

    private fun progress_bar() {

        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (childSnapshot in dataSnapshot.children) {
                    val attendance_string = childSnapshot.value.toString()
                    for(i in attendance_string){
                        if(i == 'P' || i == 'L'){
                            Total_present+=1
                        }
                    }
                }
                var total_classes = total_classes_done.text.toString().toInt()+total_classes_left.text.toString().toInt()
                var max_reachable = total_classes_left.text.toString().toInt()+Total_present
                var min_reachable = Total_present
                var current_attendance = ((Total_present / total_classes_done.text.toString().toDouble())*100).toInt()
                var max_attendance = ((max_reachable / total_classes.toDouble())*100).toInt()
                var min_attendance = ((min_reachable / total_classes.toDouble())*100).toInt()
                Handler(Looper.getMainLooper()).postDelayed({


                    Attendance_progressbar.setProgressCompat(current_attendance, true)
                    Max_progressbar.setProgressCompat(max_reachable,true)
                    Min_progressbar.setProgressCompat(min_reachable,true)
                    Attendance_progressbar_percent.setText(current_attendance.toString()+"%")
                    Max_progressbar_percent.setText(max_attendance.toString()+"%")
                    Min_progressbar_percent.setText(min_attendance.toString()+"%")

                }, 2000)

            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
                println("Error fetching data: ${databaseError.message}")
            }

        })


    }

    private fun fetch_details() {

        databaseReference2.child("Total Classes").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val total_class = dataSnapshot.child("Total").value.toString()
                val done = dataSnapshot.child("Classes Done").value.toString()

                Log.i("school", school_name)
                Log.i("Total", total_class)
                Log.i("done", done)
                val left = (total_class.toInt() - done.toInt()).toString()

                total_classes_done.setText(done)
                total_classes_left.setText(left)

            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
                println("Error fetching data: ${databaseError.message}")
            }

        })

    }

    fun onDateClick(date: CalendarAdapter.DateModel) {
        attendanceInfoText.visibility = View.VISIBLE
        val statusText = when (date.status) {
            CalendarAdapter.DateStatus.PRESENT -> "Present on ${date.date} ${getMonthName(Month_value+1)}\n$Year_value"
            CalendarAdapter.DateStatus.ABSENT -> "Absent on ${date.date} ${getMonthName(Month_value+1)}\n$Year_value"
            CalendarAdapter.DateStatus.LEAVE -> "Leave on ${date.date} ${getMonthName(Month_value+1)}\n$Year_value"
            CalendarAdapter.DateStatus.TODAY -> "Today's date"
            CalendarAdapter.DateStatus.FUTURE -> "Data Not Available"

        }
        attendanceInfoText.setText(statusText)
    }
    private fun getMonthName(month: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.MONTH, month - 1)
        return SimpleDateFormat("MMMM", Locale.getDefault()).format(calendar.time)
    }

    private fun updateMonthYearHeader(month: Int, year: Int) {

        monthName = getMonthName2(month)+" "+year
        monthYearText.setText(monthName)
    }

    private fun setUpRecyclerView(month: Int, year: Int) {
        generateDatesForMonth(month, year)
    }

    private fun generateDatesForMonth(month: Int, year: Int){

        monthName = getMonthName2(month)+" "+year

        Log.i("month", month.toString())
        Log.i("month", monthName)
        Log.i("year", year.toString())

        val calendar = Calendar.getInstance()
        calendar.set(year, month , 1)
        val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        var startPos = firstDayOfWeek - 1
        val dates = mutableListOf<CalendarAdapter.DateModel>()
        val lastDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        databaseReference.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                if (dataSnapshot.child(monthName).exists()) {

                    Log.i("Attendance", dataSnapshot.child(monthName).value.toString())
                    val attendance_data = dataSnapshot.child(monthName).value.toString()

                    val size = attendance_data.length

                    for (i in 1..lastDayOfMonth) {
                        val dateString = i.toString()
                        if (i > size) {
                            dates.add(CalendarAdapter.DateModel(dateString, CalendarAdapter.DateStatus.FUTURE))
                        } else {
                            val data = attendance_data[i - 1]
                            if (data == 'P') {
                                dates.add(
                                    CalendarAdapter.DateModel(
                                        dateString,
                                        CalendarAdapter.DateStatus.PRESENT
                                    )
                                )
                            } else if (data == 'A') {
                                dates.add(
                                    CalendarAdapter.DateModel(
                                        dateString,
                                        CalendarAdapter.DateStatus.ABSENT
                                    )
                                )
                            } else if (data == 'L') {
                                dates.add(
                                    CalendarAdapter.DateModel(
                                        dateString,
                                        CalendarAdapter.DateStatus.LEAVE
                                    )
                                )
                            } else {
                                dates.add(
                                    CalendarAdapter.DateModel(
                                        dateString,
                                        CalendarAdapter.DateStatus.FUTURE
                                    )
                                )
                            }
                        }
                    }

                    val adapter = CalendarAdapter(dates, this@Attendance)
                    recyclerViewCalendar.layoutManager = GridLayoutManager(this@Attendance, 7)
                    recyclerViewCalendar.adapter = adapter
                }else{
                    for (i in 1..lastDayOfMonth) {
                            val dateString = i.toString()
                            dates.add(CalendarAdapter.DateModel(dateString, CalendarAdapter.DateStatus.FUTURE))
                            }
                    }

                repeat(startPos) {
                    dates.add(0, CalendarAdapter.DateModel("", CalendarAdapter.DateStatus.FUTURE))
                }

                if(dates.size<42){
                    while(dates.size<42){
                        dates.add(CalendarAdapter.DateModel("",CalendarAdapter.DateStatus.FUTURE))
                    }
                }
                val adapter = CalendarAdapter(dates, this@Attendance)
                recyclerViewCalendar.layoutManager = GridLayoutManager(this@Attendance, 7)
                recyclerViewCalendar.adapter = adapter


            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
                println("Error fetching data: ${databaseError.message}")
            }

        })

//        for (i in 1..lastDayOfMonth) {
//            val dateString = i.toString()
//            //Log.i("Date",dateString)


//            val status = when {
//                calendar.get(Calendar.YEAR) > todayYear ||
//                        (calendar.get(Calendar.YEAR) == todayYear && calendar.get(Calendar.MONTH) > todayMonth) ||
//                        (calendar.get(Calendar.YEAR) == todayYear && calendar.get(Calendar.MONTH) == todayMonth && i > todayDay) -> {
//                    // For future dates
//                    CalendarAdapter.DateStatus.FUTURE
//                }
//                startPos + i % 7 == 1 -> {
//                    // For Sundays
//                    CalendarAdapter.DateStatus.HOLIDAY
//                }
//                calendar.get(Calendar.YEAR) == todayYear && calendar.get(Calendar.MONTH) == todayMonth && i == todayDay -> {
//                    // For today's date
//                    CalendarAdapter.DateStatus.TODAY
//                }
//                else -> {
//                    // For previous dates based on the status string
//                    when (dateString[i - 1]) {
//                        'P' -> CalendarAdapter.DateStatus.PRESENT
//                        'A' -> CalendarAdapter.DateStatus.ABSENT
//                        'L' -> CalendarAdapter.DateStatus.HOLIDAY
//                        else -> CalendarAdapter.DateStatus.FUTURE
//                    }
//                }
//            }

//            dates.add(CalendarAdapter.DateModel(dateString, status))
//            calendar.add(Calendar.DAY_OF_MONTH, 1)
//        }

//        repeat(startPos) {
//            dates.add(0, CalendarAdapter.DateModel("", CalendarAdapter.DateStatus.FUTURE))
//        }
//
//        return dates
    }

    private fun Intialize() {


        activity = intent.getStringExtra("Activity").toString()
        Page_name = findViewById(R.id.page_name)
        Page_name.setText(activity)
        toolbar = findViewById<Toolbar>(R.id.toolbar)
        monthYearText = findViewById(R.id.monthYearText)
        recyclerViewCalendar = findViewById(R.id.recyclerViewCalendar)
        prevMonth = findViewById(R.id.prevMonth)
        nextMonth = findViewById(R.id.nextMonth)
        //attendanceInfoLayout = findViewById(R.id.attendance_info)
        attendanceInfoText = findViewById(R.id.attendance_info_text)
        total_classes_done = findViewById(R.id.class_done)
        total_classes_left = findViewById(R.id.class_left)
        Attendance_progressbar = findViewById(R.id.attendance_progressbar)
        Attendance_progressbar_percent = findViewById(R.id.progress_percent)
        Max_progressbar = findViewById(R.id.max_progressbar)
        Min_progressbar = findViewById(R.id.min_progressbar)
        Max_progressbar_percent = findViewById(R.id.max_progress_percent)
        Min_progressbar_percent = findViewById(R.id.min_progress_percent)


        school_Id = database_internal.getString("School_Id", "1").toString()
        user_Id= database_internal.getString("User_Id", "1").toString()
        school_name= database_internal.getString("School_name", "Unknown").toString()
        class1 = "Class " +database_internal.getString("Class1", "1").toString()

        Log.i("Class", class1)
        Log.i("user_id", user_Id)
        Log.i("school_name", school_name)


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
                val intent = Intent(this@Attendance, dashboard::class.java)
                intent.putExtra("Activity", "Notification")
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