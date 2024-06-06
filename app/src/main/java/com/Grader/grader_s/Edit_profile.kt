package com.Grader.grader_s

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.Toolbar
import com.example.grader_s.R
import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class Edit_profile : AppCompatActivity() {

    private lateinit var school_Id: String
    private lateinit var user_Id: String
    private lateinit var change_password1:TextView
    private lateinit var Student_name:TextView
    private lateinit var Mother_name:TextView
    private lateinit var Father_name:TextView
    private lateinit var Address1:TextView
    private lateinit var Email_Id:TextView
    private  lateinit var user_name: String
    private lateinit var address: String
    private lateinit var mother_name: String
    private lateinit var email: String
    private lateinit var Class1: String
    private lateinit var School_name: String
    private  lateinit var father_name: String
    private lateinit var Save_button: Button
    private lateinit var toolbar: Toolbar
    private lateinit var onBackPressedCallback : OnBackPressedCallback


    private lateinit var database_internal: SharedPreferences
    private lateinit var databaseReference: DatabaseReference
    private lateinit var databaseReference2: DatabaseReference
    private lateinit var database_internal_2: SharedPreferences.Editor



    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)


        database_internal = getSharedPreferences("Database_local", MODE_PRIVATE)
        database_internal_2 = getSharedPreferences("Database_local", MODE_PRIVATE).edit()


        Intialize()
        databaseReference = Firebase.database.getReference("Users").child("Parents").child(school_Id).child(user_Id)
        databaseReference2 = Firebase.database.getReference()
        Log.i("schooid",school_Id)
        Log.i("userid",user_Id)
        back_button()
        save_details()
        change_password1()

    }

    private fun save_details() {

        Save_button.setOnClickListener(View.OnClickListener {

            val s_name = Student_name.text.toString()
            val m_name = Mother_name.text.toString()
            val f_name = Father_name.text.toString()
            val email1 = Email_Id.text.toString()
            val addres1 = Address1.text.toString()

            if (s_name.isEmpty()) {
                Toast.makeText(this, "Please Enter Student Name", Toast.LENGTH_SHORT).show()
            } else if (email1.isEmpty()) {
                Toast.makeText(this, "Please Enter Email Id", Toast.LENGTH_SHORT).show()
            } else if (addres1.isEmpty()) {
                Toast.makeText(this, "Please Enter Address", Toast.LENGTH_SHORT).show()
            } else {
                databaseReference.child("Address").setValue(addres1)
                databaseReference.child("Email").setValue(email1)
                databaseReference.child("Father Name").setValue(f_name)
                databaseReference.child("Mother Name").setValue(m_name)
                databaseReference2.child("Schools").child(School_name).child(Class1).child("Students").child(user_Id).child("Name").setValue(s_name)
                databaseReference.child("Name").setValue(s_name).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.i("Message", "Data Pushed")
                        Toast.makeText(this, "Data Saved", Toast.LENGTH_SHORT).show()

                        database_internal_2.putString("User_name", s_name)
                        database_internal_2.putString("Father_name", f_name)
                        database_internal_2.putString("Mother_name", m_name)
                        database_internal_2.putString("Address", addres1)
                        database_internal_2.putString("Email", email1)
                        database_internal_2.apply()
                        startActivity(Intent(this, Profile::class.java))
                        overridePendingTransition(R.anim.fade_in, 0)

                    } else {
                        val exception = task.exception
                        if (exception != null) {
                            exception.printStackTrace()
                        }
                        // Registration failed
                        // Handle the error, log, or show an error message to the user
                        Log.i("Message", "Failed to Push Data")
                        Toast.makeText(this, "Failed to Save Data", Toast.LENGTH_SHORT).show()

                    }
                }

            }

        })

    }

    private fun change_password1() {

        change_password1.setOnClickListener {
            startActivity(Intent(this, change_password::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

    }

    private fun back_button() {
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                startActivity(Intent(this@Edit_profile, Profile::class.java))
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right)// or perform any other action
                finish()
            }
        }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        toolbar.setNavigationOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }
    }
    private fun Intialize() {

        toolbar = findViewById<Toolbar>(R.id.toolbar)
        Save_button= findViewById(R.id.save_detail_button)
        change_password1 = findViewById(R.id.edit_text_name)
        Student_name = findViewById(R.id.student_name1)
        Address1 = findViewById(R.id.address_name1)
        Email_Id = findViewById(R.id.email_id_name1)
        Mother_name = findViewById(R.id.mother_name1)
        Father_name = findViewById(R.id.father_name1)
        change_password1.setText("Change Password")
        school_Id = database_internal.getString("School_Id", "1").toString()
        user_Id= database_internal.getString("User_Id", "1").toString()
        user_name= database_internal.getString("User_name", "Unknown").toString()
        address = database_internal.getString("Address", "1").toString()
        email = database_internal.getString("Email", "1").toString()
        father_name= database_internal.getString("Father_name", "1").toString()
        mother_name= database_internal.getString("Mother_name", "Unknown").toString()
        School_name=database_internal.getString("School_name", "Unknown").toString()
        Class1= "Class "+database_internal.getString("Class1", "Unknown").toString()

        Student_name.setText(user_name)
        Address1.setText(address)
        Email_Id.setText(email)
        Mother_name.setText(mother_name)
        Father_name.setText(father_name)

    }
}