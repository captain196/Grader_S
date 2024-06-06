package com.Grader.grader_s

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
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
import com.example.grader_s.R

class change_password : AppCompatActivity() {

    private lateinit var school_Id: String
    private lateinit var user_Id: String
    private var verificationId: String = ""
    private lateinit var verify_otp: Button
    private lateinit var send_otp: Button
    private lateinit var c1: RelativeLayout
    private lateinit var otp: EditText
    private lateinit var c3: RelativeLayout
    private lateinit var c4: TextView
    private lateinit var c5: LinearLayout
    private lateinit var c6: TextView
    private lateinit var c7: LinearLayout
    private lateinit var c8: TextView
    private lateinit var Change_password_button: Button
    private lateinit var c9: TextView
    private lateinit var c10: LinearLayout
    private lateinit var login_text: TextView
    private lateinit var create_p: EditText
    private lateinit var re_pass: EditText
    private lateinit var Phone_no: EditText
    private lateinit var alertDialog: AlertDialog
    private lateinit var toolbar: Toolbar
    private lateinit var onBackPressedCallback : OnBackPressedCallback

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var database_internal: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        auth = FirebaseAuth.getInstance()
        databaseReference = Firebase.database.getReference()
        database_internal = getSharedPreferences("Database_local", MODE_PRIVATE)

        Intialize()
        back_button()
        send_otp_funtion()
        verify_otp_funtion()
        change_password_funtion()
    }

    private fun send_otp_funtion() {

        send_otp.setOnClickListener(View.OnClickListener {

            val phone_no = Phone_no.text.toString()
            if (phone_no.isEmpty()) {
                Toast.makeText(this, "Please Enter OTP.", Toast.LENGTH_SHORT).show()
            } else if(isValidIndianPhoneNumber(phone_no)) {
                databaseReference.child("Exits").child(phone_no)
                    .addListenerForSingleValueEvent(object :
                        ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            if (dataSnapshot.exists()) {
                                alertDialog.show()
                                sendVerificationCode("+91" + phone_no)
                                section_1_active()

                            } else {
                                Toast.makeText(
                                    this@change_password,
                                    "Not Registered",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            Log.i("Message", "DataBase Error")
                        }
                    })
            }else{
                Toast.makeText(this, "Enter a valid Phone Number", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun verify_otp_funtion() {

        verify_otp.setOnClickListener {

            val verificationCode = otp.text.toString()
            if(verificationCode.isEmpty()){
                Toast.makeText(this, "Please Enter Phone no.", Toast.LENGTH_SHORT).show()
            }else {
                alertDialog.show()
                verifyCode(verificationCode)
            }
        }

    }
    private fun section_1_active() {

        c8.visibility = View.VISIBLE
        verify_otp.visibility = View.VISIBLE
        c10.visibility = View.VISIBLE
        send_otp.isEnabled = false
        Phone_no.isEnabled = false

    }

    private fun section_1_deactive() {

        c8.visibility = View.GONE
        verify_otp.visibility = View.GONE
        c10.visibility = View.GONE
        send_otp.isEnabled = true
        Phone_no.isEnabled = true

    }

    fun isValidIndianPhoneNumber(phoneNumber: String): Boolean {
        // Regular expression pattern for Indian phone numbers
        val phoneRegex = "^[7-9][0-9]{9}$"
        // Check if the phone number matches the pattern
        return phoneNumber.matches(phoneRegex.toRegex())
    }

    private fun section_2_active() {

        verify_otp.isEnabled = false
        otp.isEnabled =false
        c4.visibility = View.VISIBLE
        c5.visibility = View.VISIBLE
        c6.visibility = View.VISIBLE
        c7.visibility = View.VISIBLE
        Change_password_button.visibility = View.VISIBLE


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
                    section_1_deactive()
                    alertDialog.dismiss()
                    Toast.makeText(this@change_password, "Failed to authenticate", Toast.LENGTH_SHORT).show()

                }
                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    this@change_password.verificationId = verificationId

                    Log.i("Message1","success")
                    section_1_active()
                    alertDialog.dismiss()
                }
            }
        )
    }
    private fun verifyCode(code: String) {
        if(hasSixDigitNumber(code)) {
            val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
            signInWithPhoneAuthCredential(credential)
        }else{
            alertDialog.dismiss()
            Toast.makeText(this, "Enter 6-Digit Number", Toast.LENGTH_SHORT).show()
        }
    }

    fun hasSixDigitNumber(input: String): Boolean {
        // Regular expression pattern for a 6-digit number
        val regex = "\\b\\d{6}\\b"
        // Check if the input string contains a match
        return input.matches(".*$regex.*".toRegex())
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // User is successfully authenticated
                    Log.i("Message2","sucess")
                    alertDialog.dismiss()
                    section_2_active()
                } else {
                    alertDialog.dismiss()
                    Log.i("Message3","Failed Auth")
                    Toast.makeText(this, "Entered OTP is Wrong", Toast.LENGTH_SHORT).show()

                }
            }

    }




    private fun change_password_funtion() {

        Change_password_button.setOnClickListener(View.OnClickListener {


            alertDialog.show()
            val c_password = create_p.text.toString()
            val re_password = re_pass.text.toString()

            if (c_password.isEmpty()) {
                Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show()
                alertDialog.dismiss()
            } else if (re_password.isEmpty()) {
                Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show()
                alertDialog.dismiss()
            } else if (c_password != re_password) {
                Toast.makeText(this, "Create and Re-enter Password Not Similar", Toast.LENGTH_SHORT)
                    .show()
                alertDialog.dismiss()
            } else {
                databaseReference.child("Users").child("Parents").child(school_Id).child(user_Id).child("Password")
                    .setValue(c_password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        alertDialog.dismiss()
                        Log.i("Message", "Data Pushed")
                        Toast.makeText(this, "Password changed Sucessfully", Toast.LENGTH_SHORT).show()

                        startActivity(Intent(this@change_password, Profile::class.java))
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

    private fun back_button() {
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                startActivity(Intent(this@change_password, Edit_profile::class.java))
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
        Phone_no = findViewById(R.id.phone_number_text2)
        send_otp = findViewById(R.id.send_otp2)
        verify_otp = findViewById<Button>(R.id.verifyButton3)
        Change_password_button = findViewById<Button>(R.id.change_button2)
        create_p =findViewById<EditText>(R.id.create_password_Text2)
        re_pass = findViewById(R.id.re_enter_passsword_Text2)

        otp = findViewById<EditText>(R.id.otp_text2)
        c4 = findViewById<TextView>(R.id.create_text2)
        c5 = findViewById<LinearLayout>(R.id.l62)
        c6 = findViewById<TextView>(R.id.re_Text2)
        c7 = findViewById<LinearLayout>(R.id.l72)
        c8 = findViewById(R.id.enter_verify_text2)
        c10 = findViewById<LinearLayout>(R.id.l52)

        school_Id = database_internal.getString("School_Id", "1").toString()
        user_Id= database_internal.getString("User_Id", "1").toString()

        Log.i("school_id",school_Id)
        Log.i("user_id",user_Id)

        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this)
        val dialogView = inflater.inflate(R.layout.progressbar, null)

        dialogBuilder.setView(dialogView)
        dialogBuilder.setCancelable(false)
        alertDialog = dialogBuilder.create()



    }
}