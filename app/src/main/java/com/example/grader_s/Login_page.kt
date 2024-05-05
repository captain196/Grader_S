package com.example.grader_s

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.appCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.initialize


class Login_page : AppCompatActivity() {

    private var backPressedTime: Long = 0
    private val backPressedInterval: Long = 2000
    private lateinit var forget: MaterialTextView
    private lateinit var login: Button
    private lateinit var userID: EditText
    private lateinit var schoolID: EditText
    private lateinit var password: TextInputEditText
    private lateinit var alertDialog: AlertDialog


    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var database_internal: SharedPreferences
    private lateinit var database_internal_2: SharedPreferences.Editor

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)

        auth = FirebaseAuth.getInstance()


        databaseReference = Firebase.database.getReference("Users").child("Parents")
        database_internal = getSharedPreferences("Database_local", MODE_PRIVATE)
        database_internal_2 = getSharedPreferences("Database_local", MODE_PRIVATE).edit()

        Intialize()
        back_button()

        val logg_in = database_internal.getBoolean("Logged_value",false)

        if(logg_in){
            startActivity(Intent(this@Login_page, dashboard::class.java))
            finish()
        }else {


            login.setOnClickListener(View.OnClickListener {

                val user = userID.text.toString()
                val school = schoolID.text.toString()
                val pass = password.text.toString()
                Log.i("Message", user)
                Log.i("Message", school)
                Log.i("Message", pass)

                if (user.isEmpty()) {
                    Toast.makeText(this, "Please Enter User ID", Toast.LENGTH_SHORT).show()
                } else if (school.isEmpty()) {
                    Toast.makeText(this, "Please Enter School ID", Toast.LENGTH_SHORT).show()
                } else if (pass.isEmpty()) {
                    Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show()
                } else {
                    alertDialog.show()
                    databaseReference.child(school).addListenerForSingleValueEvent(object :
                        ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            if (dataSnapshot.exists()) {

                                Log.i("Message", "Data exists")

                                databaseReference.child(school).child(user)
                                    .addListenerForSingleValueEvent(object :
                                        ValueEventListener {
                                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                                            if (dataSnapshot.exists()) {

                                                val value = dataSnapshot.child("Password")
                                                    .getValue(String::class.java)
                                                if (pass == value) {

                                                    Log.i("Message", "Password match")
                                                    val value5 = dataSnapshot.child("Phone Number")
                                                        .getValue(String::class.java)
                                                    val value2 = dataSnapshot.child("Name")
                                                        .getValue(String::class.java)
                                                    val address = dataSnapshot.child("Address")
                                                        .getValue(String::class.java)
                                                    val father_name =
                                                        dataSnapshot.child("Father Name")
                                                            .getValue(String::class.java)
                                                    val mother_name =
                                                        dataSnapshot.child("Mother Name")
                                                            .getValue(String::class.java)
                                                    val email = dataSnapshot.child("Email")
                                                        .getValue(String::class.java)
                                                    val password1 = dataSnapshot.child("Password")
                                                        .getValue(String::class.java)
                                                    val Class = dataSnapshot.child("Class")
                                                        .getValue(String::class.java)
                                                    val school_name =
                                                        dataSnapshot.child("School Name")
                                                            .getValue(String::class.java)

                                                    create_user(
                                                        email.toString(),
                                                        password.toString()
                                                    )
                                                    signInWithEmailAndPassword(
                                                        email.toString(),
                                                        password.toString()
                                                    )
                                                    Log.i("Message", "auth done")

                                                    val editor: SharedPreferences.Editor =
                                                        getSharedPreferences(
                                                            "Database_local",
                                                            MODE_PRIVATE
                                                        ).edit()
                                                    editor.putString("School_Id", school)
                                                    editor.putString("Phone_no", value5)
                                                    editor.putString("User_Id", user)
                                                    editor.putString("User_name", value2)
                                                    editor.putString("Class1", Class)
                                                    editor.putString("Address", address)
                                                    editor.putString("Father_name", father_name)
                                                    editor.putString("Mother_name", mother_name)
                                                    editor.putString("Email", email)
                                                    editor.putString("Password", password1)
                                                    editor.putString("School_name", school_name)
                                                    editor.putBoolean("Logged_value", true)
                                                    editor.apply()

                                                    alertDialog.dismiss()

                                                    val intent =
                                                        Intent(
                                                            this@Login_page,
                                                            dashboard::class.java
                                                        )
                                                    startActivity(intent)
                                                    overridePendingTransition(R.anim.fade_in, 0)
                                                    finish()
                                                } else {
                                                    Toast.makeText(
                                                        this@Login_page,
                                                        "Please Enter Correct Password",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    alertDialog.dismiss()
                                                }

                                            } else {
                                                Toast.makeText(
                                                    this@Login_page,
                                                    "Please Enter Correct User ID",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                alertDialog.dismiss()
                                            }
                                        }
                                        override fun onCancelled(databaseError: DatabaseError) {
                                            Log.i("Message", "DataBase Error")
                                            alertDialog.dismiss()
                                        }
                                    })

                            } else {
                                Toast.makeText(
                                    this@Login_page,
                                    "Please Enter Correct School ID",
                                    Toast.LENGTH_SHORT
                                ).show()
                                alertDialog.dismiss()
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            Log.i("Message", "DataBase Error")
                        }
                    })
                }
            })
            forget.setOnClickListener {
                startActivity(Intent(this, forget_page::class.java))
                overridePendingTransition(R.anim.fade_in, 0)
            }
        }
    }

    private fun signInWithEmailAndPassword(email: String, password: String) {

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    Log.i("auth",auth.currentUser.toString())

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }

    private fun create_user(email: String, password: String) {

        Log.i("Message","inside")
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    Toast.makeText(
                        baseContext, "User created with email: ${user?.email}",
                        Toast.LENGTH_SHORT
                    ).show()
                    // TODO: Proceed to next activity or perform necessary actions
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun Intialize() {

        forget = findViewById(R.id.forget_text)
        login = findViewById<Button>(R.id.loginButton)
        userID = findViewById(R.id.userIDEditText)
        schoolID = findViewById(R.id.schoolIDEditText)
        password = findViewById(R.id.passwordEditText)

        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this)
        val dialogView = inflater.inflate(R.layout.progressbar, null)

        dialogBuilder.setView(dialogView)
        dialogBuilder.setCancelable(false)
        alertDialog = dialogBuilder.create()

    }

    private fun back_button() {

        onBackPressedDispatcher.addCallback(this,object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {

                if (backPressedTime + backPressedInterval > System.currentTimeMillis()) {
                    finishAffinity()
                } else {
                    Toast.makeText(applicationContext, "Press again to exit", Toast.LENGTH_SHORT).show()
                }
                backPressedTime = System.currentTimeMillis()
            }
        })
    }
}