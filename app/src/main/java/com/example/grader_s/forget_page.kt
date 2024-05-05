package com.example.grader_s

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.location.Address
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import java.util.concurrent.TimeUnit

class forget_page : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var verificationId: String = ""
    private var varificationcode: String =""
    private lateinit var pno: EditText
    private lateinit var send_otp: Button
    private lateinit var verify_otp: Button
    private lateinit var otp: EditText
    private lateinit var alertDialog: AlertDialog
    private lateinit var enter_text: TextView
    private lateinit var toolbar: Toolbar
    private lateinit var onBackPressedCallback : OnBackPressedCallback

    private lateinit var databaseReference: DatabaseReference
    private lateinit var database_internal: SharedPreferences
    private lateinit var database_internal_2: SharedPreferences.Editor


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_page)

        Intialize()
        back_button()

        auth = FirebaseAuth.getInstance()
        databaseReference = Firebase.database.getReference()
        database_internal = getSharedPreferences("Database_local", MODE_PRIVATE)
        database_internal_2 = getSharedPreferences("Database_local", MODE_PRIVATE).edit()


        send_otp.setOnClickListener(View.OnClickListener {

            val p = pno.text.toString()
            if(p.isEmpty()){
                Toast.makeText(this, "Please Enter Phone Number", Toast.LENGTH_SHORT).show()
            }else{
                databaseReference.child("Exits").child(p).addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            alertDialog.show()
                            sendVerificationCode("+91"+p)

                        } else {
                            Toast.makeText(this@forget_page, "Not Registered", Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onCancelled(databaseError: DatabaseError) {
                        Log.i("Message","DataBase Error")
                    }
                })

            }

        })

        verify_otp.setOnClickListener(View.OnClickListener {

            val p = otp.text.toString()
            if(p.isEmpty()){
                Toast.makeText(this, "Please Enter Phone Number", Toast.LENGTH_SHORT).show()
            }else{
                alertDialog.show()
                verifyCode(p)
            }

        })




    }

    private fun sendVerificationCode(phoneNumber: String) {
        // Re-authenticate the user if needed
        val user = auth.currentUser
        if (user != null) {
            user.getIdToken(true)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // User re-authenticated successfully
                        performPhoneAuth(phoneNumber)
                    } else {
                        // Re-authentication failed
                        Log.i("Message-1","sucess")
                        alertDialog.dismiss()
                        Toast.makeText(this, "Failed to authenticate", Toast.LENGTH_SHORT).show()
                    }

                }
        } else {
            // User is not signed in, perform phone authentication directly
            performPhoneAuth(phoneNumber)
        }
    }

    private fun performPhoneAuth(phoneNumber: String) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber,
            60,
            TimeUnit.SECONDS,
            this,
            object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    signInWithPhoneAuthCredential(credential)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    Log.i("Message0",e.message.toString())
                    alertDialog.dismiss()
                    Toast.makeText(this@forget_page, "Failed to authenticate", Toast.LENGTH_SHORT).show()

                }
                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    this@forget_page.verificationId = verificationId

                    Log.i("Message1","success")
                    alertDialog.dismiss()
                    section_1_active()
                }
            }
        )
    }
    private fun verifyCode(code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        signInWithPhoneAuthCredential(credential)
    }

    private fun section_1_active() {

        enter_text.visibility = View.VISIBLE
        verify_otp.visibility = View.VISIBLE
        otp.visibility = View.VISIBLE
        send_otp.isEnabled = false
        pno.isEnabled = false

    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // User is successfully authenticated
                    Log.i("Message2","sucess")
                    saveLoginState(true)
                    Toast.makeText(this, "Sucessfully Authenticate", Toast.LENGTH_SHORT).show()
                    alertDialog.dismiss()

                    databaseReference.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            // This method is called once with the initial value and again
                            // whenever data at this location is updated.
                            val value2 = dataSnapshot.child("User_ids_pno").child(pno.text.toString()).getValue(String::class.java)
                            val value = dataSnapshot.child("Exits").child(pno.text.toString()).getValue(String::class.java)
                            val value3 = dataSnapshot.child("Users").child("Parents").child(value.toString()).child(value2.toString()).child("Name").getValue(String::class.java)
                            val address = dataSnapshot.child("Users").child("Parents").child(value.toString()).child(value2.toString()).child("Address").getValue(String::class.java)
                            val father_name = dataSnapshot.child("Users").child("Parents").child(value.toString()).child(value2.toString()).child("Father Name").getValue(String::class.java)
                            val mother_name = dataSnapshot.child("Users").child("Parents").child(value.toString()).child(value2.toString()).child("Mother Name").getValue(String::class.java)
                            val email = dataSnapshot.child("Users").child("Parents").child(value.toString()).child(value2.toString()).child("Email").getValue(String::class.java)
                            val password = dataSnapshot.child("Users").child("Parents").child(value.toString()).child(value2.toString()).child("Password").getValue(String::class.java)
                            val Class = dataSnapshot.child("Users").child("Parents").child(value.toString()).child(value2.toString()).child("Class").getValue(String::class.java)
                            val school_name = dataSnapshot.child("Users").child("Parents").child(value.toString()).child(value2.toString()).child("School Name").getValue(String::class.java)

                            val editor: SharedPreferences.Editor = getSharedPreferences("Database_local", MODE_PRIVATE).edit()
                            editor.putString("School_Id", value.toString())
                            editor.putString("Phone_no",pno.text.toString())
                            editor.putString("User_Id",value2.toString())
                            editor.putString("User_name", value3.toString())
                            editor.putString("Class1", Class)
                            editor.putString("Address",address)
                            editor.putString("Father_name",father_name)
                            editor.putString("Mother_name", mother_name)
                            editor.putString("Email", email)
                            editor.putString("Password", password)
                            editor.putString("School_name", school_name)
                            editor.putBoolean("Logged_value", true)
                            editor.apply()

                            val intent = Intent(this@forget_page, dashboard::class.java)

                            startActivity(intent)
                            overridePendingTransition(R.anim.fade_in,0)
                            finish()

                        }

                        override fun onCancelled(error: DatabaseError) {
                            // Failed to read value
                        }
                    })

                } else {
                    // Authentication failed
                    Log.i("Message3","Failed Auth")
                    Toast.makeText(this, "Failed to send OTP", Toast.LENGTH_SHORT).show()

                }
            }

    }


    private fun Intialize() {

        toolbar = findViewById<Toolbar>(R.id.toolbar)
        pno = findViewById(R.id.phone_number_text1)
        send_otp = findViewById(R.id.send_otp)
        verify_otp = findViewById(R.id.verifyButton2)
        otp = findViewById(R.id.otp_text1)
        enter_text = findViewById(R.id.enter_verify_text)

        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this)
        val dialogView = inflater.inflate(R.layout.progressbar, null)

        dialogBuilder.setView(dialogView)
        dialogBuilder.setCancelable(false)
        alertDialog = dialogBuilder.create()

    }

    private fun saveLoginState(isLoggedIn: Boolean) {
        database_internal_2.putBoolean("is_logged_in",isLoggedIn)
        database_internal_2.apply()
    }

    private fun back_button() {
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                startActivity(Intent(this@forget_page, Login_page::class.java))
                overridePendingTransition(R.anim.fade_in,0)// or perform any other action
                finish()
            }
        }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        toolbar.setNavigationOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }
    }
}