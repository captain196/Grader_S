package com.Grader.grader_s

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.example.grader_s.R
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class register_2 : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var verificationId: String
    private lateinit var firstname: String
    private lateinit var lastname: String
    private lateinit var pno: String
    private lateinit var address: String
    private lateinit var email: String
    private lateinit var school_id: String
    private lateinit var verify_otp: Button
    private lateinit var c1: RelativeLayout
    private lateinit var otp: EditText
    private lateinit var c3: RelativeLayout
    private lateinit var c4: TextView
    private lateinit var c5: LinearLayout
    private lateinit var c6: TextView
    private lateinit var c7: LinearLayout
    private lateinit var register: Button
    private lateinit var c9: TextView
    private lateinit var user_id: TextView
    private lateinit var login_text: TextView
    private lateinit var create_p: EditText
    private lateinit var re_pass: EditText

    private lateinit var alertDialog: AlertDialog



    private lateinit var databaseReference: DatabaseReference



    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register2)

        Intialize()
        getdata()


        auth = FirebaseAuth.getInstance()
        databaseReference = Firebase.database.getReference()


        verify_otp.setOnClickListener {
            alertDialog.show()
            val verificationCode = otp.text.toString()
            verifyCode(verificationCode)
        }

        register.setOnClickListener(View.OnClickListener {

            alertDialog.show()
            data_push()
            section_2_active()
        })

        login_text.setOnClickListener{
            startActivity(Intent(this, Login_page::class.java))
            overridePendingTransition(R.anim.fade_in,0)
        }


    }

    private fun section_2_active() {

        create_p.isEnabled = false
        re_pass.isEnabled = false
        create_p.setBackgroundResource(R.drawable.disable_background_box)
        re_pass.setBackgroundResource(R.drawable.disable_background_box)
        register.isEnabled = false
        c9.visibility = View.VISIBLE
        user_id.visibility = View.VISIBLE
        login_text.visibility = View.VISIBLE



    }


    private fun verifyCode(code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // User is successfully authenticated
                    Log.i("Message3", "sucess")
                    Toast.makeText(this, "SucessFully Authenticate", Toast.LENGTH_SHORT).show()
                    alertDialog.dismiss()
                    section_1_active()

                } else {
                    // Authentication failed
                    Log.i("Message3", "Failed Auth")
                    Toast.makeText(this, "Failed to send OTP", Toast.LENGTH_SHORT).show()

                }
            }
    }

    private fun section_1_active() {

        otp.isEnabled = false
        otp.setBackgroundResource(R.drawable.disable_background_box)
        verify_otp.isEnabled = false
        c4.visibility = View.VISIBLE
        c5.visibility = View.VISIBLE
        c6.visibility = View.VISIBLE
        c7.visibility = View.VISIBLE
        register.visibility = View.VISIBLE
    }

    private fun data_push() {

        val password = create_p.text.toString()
        val users =
            data_class_register(firstname, lastname, school_id, address, email, pno,password)
        Log.i("M1", "Data Pushing")
        databaseReference.child("Users").child(school_id).setValue(users)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.i("Message", "Data Pushed")
                    alertDialog.dismiss()
                    Toast.makeText(this, "User Registered", Toast.LENGTH_SHORT).show()

                } else {
                    val exception = task.exception
                    if (exception != null) {
                        exception.printStackTrace()
                    }
                    // Registration failed
                    // Handle the error, log, or show an error message to the user
                    Log.i("Message", "Failed to Push Data")
                    Toast.makeText(this, "Failed to Register", Toast.LENGTH_SHORT).show()

                }
            }
    }

    private fun getdata() {

        verificationId = intent.getStringExtra("verificationId").toString()
        firstname = intent.getStringExtra("fname").toString()
        lastname = intent.getStringExtra("lname").toString()
        address = intent.getStringExtra("address").toString()
        email = intent.getStringExtra("email").toString()
        pno = intent.getStringExtra("phone_no").toString()
        school_id = intent.getStringExtra("school_id").toString()

    }

    private fun Intialize() {
//
//        c1 = findViewById<RelativeLayout>(R.id.verfy_text_l)
//        otp = findViewById<EditText>(R.id.otp_text)
//        c4 = findViewById<TextView>(R.id.create_text)
//        c5 = findViewById<LinearLayout>(R.id.l6)
//        c6 = findViewById<TextView>(R.id.re_Text)
//        c7 = findViewById<LinearLayout>(R.id.l7)
//        register = findViewById<Button>(R.id.registerButton)
//        verify_otp = findViewById<Button>(R.id.verifyButton)
//        create_p =findViewById<EditText>(R.id.create_password_Text)
//        re_pass = findViewById(R.id.re_enter_passsword_Text)
//
//        val dialogBuilder = AlertDialog.Builder(this)
//        val inflater = LayoutInflater.from(this)
//        val dialogView = inflater.inflate(R.layout.progressbar, null)
//
//        dialogBuilder.setView(dialogView)
//        dialogBuilder.setCancelable(false)
//        alertDialog = dialogBuilder.create()
    }
}