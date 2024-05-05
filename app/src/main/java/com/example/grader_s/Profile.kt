package com.example.grader_s

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.Toolbar
import coil.load
import coil.transform.CircleCropTransformation
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class Profile : AppCompatActivity() {

    private val PICK_IMAGE_REQUEST = 1
    private lateinit var school_Id: String
    private lateinit var phone_no: String
    private lateinit var user_Id: String
    private lateinit var user_name: String
    private lateinit var address: String
    private lateinit var gaurdian_name: String
    private lateinit var school_name: String
    private lateinit var password: String
    private lateinit var Class1: String
    private lateinit var email: String
    private lateinit var auth: FirebaseAuth
    private lateinit var School_name: TextView
    private lateinit var User_name: TextView
    private lateinit var Class: TextView
    private lateinit var Gaurdian_name: TextView
    private lateinit var Phone_no: TextView
    private lateinit var Email: TextView
    private lateinit var Address1: TextView
    private lateinit var School_Id: TextView
    private lateinit var User_Id: TextView
    private lateinit var toolbar: Toolbar
    private lateinit var edit: TextView
    private lateinit var alertDialog: AlertDialog
    private lateinit var School_logo: ImageView
    private lateinit var Profile_image: ImageView
    private lateinit var onBackPressedCallback :OnBackPressedCallback

    private lateinit var RelativeLayout_profile_image: RelativeLayout

    private lateinit var databaseReference: DatabaseReference
    private lateinit var database_internal: SharedPreferences
    private lateinit var database_internal_2: SharedPreferences.Editor


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        databaseReference = Firebase.database.getReference()
        database_internal = getSharedPreferences("Database_local", MODE_PRIVATE)
        database_internal_2 = getSharedPreferences("Database_local", MODE_PRIVATE).edit()

        Intialize()
        fetch_datails()
        back_button()
        edit_button()
        profile_click()

    }

    private fun profile_click() {
        RelativeLayout_profile_image.setOnClickListener(View.OnClickListener {
            selectImage()
        })
    }

    private fun edit_button() {

        edit.setOnClickListener(){
            startActivity(Intent(this, Edit_profile::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

        }
    }

    private fun back_button() {
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                startActivity(Intent(this@Profile, dashboard::class.java))
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right)// or perform any other action
                finish()
            }
        }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        toolbar.setNavigationOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }
    }

    private fun save_data(){

        School_name.setText(school_name)
        User_name.setText(user_name)
        Gaurdian_name.setText(gaurdian_name)
        Email.setText(email)
        Phone_no.setText("+91 "+phone_no)
        Address1.setText(address)
        School_Id.setText(school_Id)
        Class.setText(Class1)
        User_Id.setText("ID - "+user_Id)


    }

    private fun Intialize() {

        RelativeLayout_profile_image = findViewById(R.id.r12)
        Profile_image = findViewById(R.id.profile_photo)
        School_logo = findViewById(R.id.school_logo)
        toolbar = findViewById<Toolbar>(R.id.toolbar)
        edit = findViewById<TextView>(R.id.edit_text_name)
        School_name = findViewById(R.id.school_name)
        User_name = findViewById(R.id.profile_name)
        Class = findViewById(R.id.class_name)
        Address1 = findViewById(R.id.Address_id_text)
        School_Id = findViewById(R.id.school_id_text)
        User_Id = findViewById(R.id.profile_id)
        Email = findViewById(R.id.email_id_text)
        Gaurdian_name = findViewById(R.id.parent_name_text)
        Phone_no = findViewById(R.id.contact_text)

        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this)
        val dialogView = inflater.inflate(R.layout.progressbar, null)

        dialogBuilder.setView(dialogView)
        dialogBuilder.setCancelable(false)
        alertDialog = dialogBuilder.create()

    }

    private fun fetch_datails() {

        phone_no = database_internal.getString("Phone_no", "1").toString()

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value2 =
                    dataSnapshot.child("User_ids_pno").child(phone_no).getValue(String::class.java).toString()
                val value = dataSnapshot.child("Exits").child(phone_no).getValue(String::class.java).toString()
                val value3 =
                    dataSnapshot.child("Users").child("Parents").child(value.toString()).child(value2.toString())
                        .child("Name").getValue(String::class.java).toString()
                val Address1 =
                    dataSnapshot.child("Users").child("Parents").child(value.toString()).child(value2.toString())
                        .child("Address").getValue(String::class.java).toString()
                val Father_name1 =
                    dataSnapshot.child("Users").child("Parents").child(value.toString()).child(value2.toString())
                        .child("Father Name").getValue(String::class.java).toString()
                val Mother_name1 =
                    dataSnapshot.child("Users").child("Parents").child(value.toString()).child(value2.toString())
                        .child("Mother Name").getValue(String::class.java).toString()
                val Email1 =
                    dataSnapshot.child("Users").child("Parents").child(value.toString()).child(value2.toString())
                        .child("Email").getValue(String::class.java).toString()
                val Password1 =
                    dataSnapshot.child("Users").child("Parents").child(value.toString()).child(value2.toString())
                        .child("Password").getValue(String::class.java).toString()
                val Class12 =
                    dataSnapshot.child("Users").child("Parents").child(value.toString()).child(value2.toString())
                        .child("Class").getValue(String::class.java).toString()
                val School_name12 =
                    dataSnapshot.child("Users").child("Parents").child(value.toString()).child(value2.toString())
                        .child("School Name").getValue(String::class.java).toString()
                val logo_link = dataSnapshot.child("Schools").child(School_name12).child("Logo").getValue(String::class.java).toString()
                val profileImage = dataSnapshot.child("Schools").child(School_name12).child("Class "+Class12).child("Students").child(value2).child("Profile").child("image").value.toString()


                database_internal_2.putString("School_Id", value.toString())
                database_internal_2.putString("Phone_no", phone_no)
                database_internal_2.putString("User_Id", value2.toString())
                database_internal_2.putString("User_name", value3.toString())
                database_internal_2.putString("Class1", Class12)
                database_internal_2.putString("Address", Address1)
                database_internal_2.putString("Father_name", Father_name1)
                database_internal_2.putString("Mother_name",Mother_name1)
                database_internal_2.putString("Email", Email1)
                database_internal_2.putString("Password", Password1)
                database_internal_2.putString("School_name", School_name12)
                database_internal_2.apply()

                school_Id =value.toString()
                phone_no = database_internal.getString("Phone_no", "1").toString()
                user_Id= value2.toString()
                user_name= value3.toString()
                address = Address1.toString()
                email = Email1.toString()
                val father_name= Father_name1.toString()
                val mother_name= Mother_name1.toString()
                password = Password1.toString()
                Class1 = Class12.toString()
                school_name = School_name12.toString()

                if (father_name != ""){
                    gaurdian_name = father_name
                }else{
                    gaurdian_name = mother_name
                }

                Log.d("MainActivity", "Value is: $value")
                School_logo.load(logo_link) {
                    placeholder(R.drawable.transparent)
                    error(R.drawable.logo_sc)

                    crossfade(true)
                    // Additional options can be added here
                }

                Profile_image.load(profileImage) {
                    placeholder(R.drawable.transparent)
                    error(R.drawable.profile_logo)
                    transformations(CircleCropTransformation())
                    crossfade(true)
                }
                save_data()

            }
            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })
    }


    // Function to upload an image to Firebase Storage
    private fun uploadImageToStorage(imageUri: Uri) {
        alertDialog.show()
        var class1 = Class1.split(" ")[0]
        val storageRef = FirebaseStorage.getInstance().reference
        val imageRef = storageRef.child(school_name+"/Class "+class1 +"/"+user_Id+"/profile_icon.png")

        imageRef.putFile(imageUri)
            .addOnSuccessListener { taskSnapshot ->
                // Image uploaded successfully
                alertDialog.dismiss()
                Toast.makeText(this, "Image uploaded successfully", Toast.LENGTH_SHORT).show()

                // Get the download URL of the uploaded image
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    val imageUrl = uri.toString()
                    databaseReference.child("Schools").child(school_name).child("Class "+Class1).child("Students").child(user_Id).child("Profile").child("image").setValue(imageUrl.toString())
                    //Toast.makeText(this, "Image uploaded successfully", Toast.LENGTH_SHORT).show()
                    // Here you can save the imageUrl to Realtime Database or perform any other action
                }
            }
            .addOnFailureListener { exception ->
                // Handle errors
                Toast.makeText(this, "Failed to upload image: ${exception.message}", Toast.LENGTH_SHORT).show()
            }

    }

    // Function to handle image selection from gallery or camera
    private fun selectImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    // Handle the result of the image selection
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val imageUri = data.data
            // Upload the selected image to Firebase Storage
            imageUri?.let { uploadImageToStorage(it) }
        }
    }

}