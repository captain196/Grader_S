package com.Grader.grader_s

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import com.example.grader_s.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class register : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var verificationId: String = ""
    private lateinit var send_otp_text: Button
    private lateinit var log_text: TextView
    private lateinit var firstname: EditText
    private lateinit var lastname: EditText
    private lateinit var pno: EditText
    private lateinit var address: EditText
    private lateinit var email: EditText
    private lateinit var school_id: EditText
    private lateinit var F1: FrameLayout
    private lateinit var outer_progess: ProgressBar
    private lateinit var inner_progress: ProgressBar
    private lateinit var alertDialog: AlertDialog


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        Intialize()

        auth = FirebaseAuth.getInstance()


        send_otp_text.setOnClickListener{
            alertDialog.show()
            sendVerificationCode("+91"+pno.text.toString())
        }

        log_text.setOnClickListener {

            startActivity(Intent(this, Login_page::class.java))
            overridePendingTransition(R.anim.fade_in,0)
        }

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
                    }
                    override fun onCodeSent(
                        verificationId: String,
                        token: PhoneAuthProvider.ForceResendingToken
                    ) {
                        this@register.verificationId = verificationId
                        Log.i("Message1","success")
                        alertDialog.dismiss()
                        val intent = Intent(this@register, register_2::class.java)
                        intent.putExtra("verificationId", verificationId)
                        intent.putExtra("fname", firstname.text.toString())
                        intent.putExtra("lname", lastname.text.toString())
                        intent.putExtra("email", email.text.toString())
                        intent.putExtra("address", address.text.toString())
                        intent.putExtra("phone_no", pno.text.toString())
                        intent.putExtra("school_id", school_id.text.toString())
                        startActivity(intent)
                        finish()
                    }
                }
            )
        }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
    auth.signInWithCredential(credential)
        .addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                // User is successfully authenticated
                Log.i("Message2","sucess")
                Toast.makeText(this, "Otp Sent +91 ${pno.text.toString()}", Toast.LENGTH_SHORT).show()
            } else {
                // Authentication failed
                Log.i("Message3","Failed Auth")
                Toast.makeText(this, "Failed to send OTP", Toast.LENGTH_SHORT).show()

            }
        }
    }

    fun Intialize() {


        firstname = findViewById<EditText>(R.id.first_name_Text)
        lastname = findViewById<EditText>(R.id.last_name_Text)
        school_id = findViewById<EditText>(R.id.school_code_Text)
        address = findViewById<EditText>(R.id.address_Text)
        email = findViewById<EditText>(R.id.email_id_Text)
        pno = findViewById<EditText>(R.id.phone_number_text)
        send_otp_text = findViewById<Button>(R.id.send_otp_button)
        log_text = findViewById<TextView>(R.id.login_text_register)
        F1 = findViewById<FrameLayout>(R.id.f1)
        outer_progess = findViewById(R.id.outerProgressBar)
        inner_progress = findViewById(R.id.innerProgressBar)

        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this)
        val dialogView = inflater.inflate(R.layout.progressbar, null)

        dialogBuilder.setView(dialogView)
        dialogBuilder.setCancelable(false)
        alertDialog = dialogBuilder.create()

    }
}