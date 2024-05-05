package com.example.grader_s

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.models.SlideModel
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.navigation.NavigationView
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.appCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.initialize
import com.google.gson.Gson


class dashboard : AppCompatActivity() {

    private var backPressedTime: Long = 0
    private val backPressedInterval: Long = 2000
    private lateinit var School_Id: String
    private lateinit var Phone_no: String
    private lateinit var User_Id: String
    private var School_name: String = ""
    private var Class: String = ""
    private lateinit var auth: FirebaseAuth
    private lateinit var drawer_layout: DrawerLayout
    private lateinit var navi_layout: NavigationView
    private lateinit var toolbar: Toolbar
    private lateinit var r1: RecyclerView
    private lateinit var r2: RecyclerView
    private lateinit var Name: TextView
    private var User_name: String = ""
    private lateinit var image: ImageView
    private lateinit var navi_student_name: MaterialTextView
    private var a1: adapter_subject = adapter_subject(listOf())
    private var a2: adapter_features = adapter_features(listOf())
    private lateinit var shimmer_1: ShimmerFrameLayout
    private lateinit var shimmer_2: ShimmerFrameLayout
    private lateinit var Shimmer_layout: LinearLayout
    private lateinit var gson: Gson
    private lateinit var navi_name: TextView
    private lateinit var navi_profile_image: ImageView
    private lateinit var School_logo: ImageView
    private lateinit var image_slider: ImageSlider
    private var imageList = ArrayList<SlideModel>()


    private var dataclass_subject_list: MutableList<dataclass_subject> = mutableListOf()
    private var dataclass_feature_list: MutableList<dataclass_subject> = mutableListOf()

    private lateinit var databaseReference: DatabaseReference
    private lateinit var database_internal: SharedPreferences
    private lateinit var database_internal_2: SharedPreferences.Editor


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)


        auth = FirebaseAuth.getInstance()
        FirebaseApp.initializeApp(this)
        Firebase.initialize(context = this)
        Firebase.appCheck.installAppCheckProviderFactory(
            PlayIntegrityAppCheckProviderFactory.getInstance(),)

        databaseReference = Firebase.database.getReference()
        database_internal = getSharedPreferences("Database_local", MODE_PRIVATE)
        database_internal_2 = getSharedPreferences("Database_local", MODE_PRIVATE).edit()
        gson = Gson()

        if (!isLogg_in()) {
            // User is signed in
            startActivity(Intent(this@dashboard, Login_page::class.java))
            finish()
        } else {

            fetch_datails()
            Intialize()
            back_button()
            navigation_control()
            update_name()
            update_logo()
            //image_slider()
            //get_school_name_class()
            //showShimmerEffect()
            get_subjects()
            get_features()


//            Log.i("data_feature",dataclass_feature_list.toString())
//            a2 = adapter_features(dataclass_feature_list)
//            r2.layoutManager = GridLayoutManager(this,2)
//            r2.adapter = a2
//            shimmer_1.stopShimmer()
//            shimmer_2.stopShimmer()
//            Shimmer_layout.visibility = View.GONE

           // on_click_feature()
            //click_feature()
            }
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

//    private fun image_slider() {
//
//        databaseReference.child("Schools").child(School_name).child("Activities").addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//
//                for(chidsnapshot in dataSnapshot.children) {
//
//                    val value = chidsnapshot.value.toString()
//                    Log.i("Link",value)
//                    imageList.add(SlideModel(value))
//                    image_slider.setImageList(imageList)
//                }
//            }
//            override fun onCancelled(databaseError: DatabaseError) {
//                // Handle error
//                println("Error fetching data: ${databaseError.message}")
//            }
//
//        })
//
//
//    }

    private fun update_logo() {
        databaseReference.child("Schools").child(School_name).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val navi_image_value = dataSnapshot.child(Class).child("Students").child(User_Id).child("Profile").child("image").value.toString()
                val value = dataSnapshot.child("Logo").value.toString()

                School_logo.load(value) {
                    placeholder(R.drawable.transparent)
                    error(R.drawable.logo_sc)
                    crossfade(true)
                }

                navi_profile_image.load(navi_image_value) {
                    placeholder(R.drawable.transparent)
                    error(R.drawable.logo_sc)
                    transformations(CircleCropTransformation())
                    crossfade(true)
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
                println("Error fetching data: ${databaseError.message}")
            }

        })

    }

    private fun get_features() {

        databaseReference.child("Schools").child(School_name).child("Features").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                // Check if dataSnapshot has children
                for (childSnapshot in dataSnapshot.children) {

                    val key = childSnapshot.key.toString()

                    val drawableName = key.decapitalize()
                    Log.i("feature_name",drawableName)// Replace "your_drawable_name" with the name of your drawable file without extension
                    val resourceId = getDrawableResourceId(this@dashboard, drawableName)

                    dataclass_feature_list.add(dataclass_subject(key,resourceId))

                    //load_image_features(key.toString(),childSnapshot.value.toString(),total)
                }
                Log.i("Subjects",dataclass_feature_list.toString())
                a2 = adapter_features(dataclass_feature_list)
                r2.layoutManager = GridLayoutManager(this@dashboard,2)
                r2.adapter = a2
                shimmer_1.stopShimmer()
                shimmer_2.stopShimmer()
                Shimmer_layout.visibility = View.GONE

                on_click_feature()
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
                println("Error fetching data: ${databaseError.message}")
            }

        })
    }

//    private fun load_image_features(key: String,image_string: String, total: Int) {
//
//        val storage = FirebaseStorage.getInstance()
//        val storageRef = storage.reference
//
//        val localFile = File.createTempFile("images", "jpg")
//        val imageRef = storageRef.child(image_string)
//
//        // Download image to local file
//        imageRef.getFile(localFile).addOnSuccessListener {
//            // Image downloaded successfully
//            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
//            dataclass_feature_list.add(dataclass_subject(key,bitmap))
//
//            Log.i("Message",dataclass_feature_list.toString())
//
//            if(dataclass_feature_list.size == total){
//
//
//                a2 = adapter_features(dataclass_feature_list)
//                r2.layoutManager = GridLayoutManager(this,2)
//                r2.adapter = a2
//                shimmer_1.stopShimmer()
//                shimmer_2.stopShimmer()
//                Shimmer_layout.visibility = View.GONE
//
//                on_click_feature()
//
//            }
//
//            // Now you can use 'imageData' wherever you need the image resource ID
//        }.addOnFailureListener { exception ->
//            exception.printStackTrace()
//        }
//
//
//    }

    private fun on_click_feature() {

        a2.setOnItemClickListener { position ->

            val feature = dataclass_feature_list[position].title

            if (feature == "Attendance") {
                val intent = Intent(this, Attendance::class.java)
                intent.putExtra("Activity", feature)
                startActivity(intent)
                overridePendingTransition(R.anim.fade_in,0)
            } else if (feature == "Notification") {
                val intent = Intent(this, Notification::class.java)
                intent.putExtra("Activity", feature)
                startActivity(intent)
                overridePendingTransition(R.anim.fade_in,0)
            } else if (feature == "Assignment") {
                val intent = Intent(this, Assignment::class.java)
                intent.putExtra("Activity", feature)
                startActivity(intent)
                overridePendingTransition(R.anim.fade_in,0)
            }else if (feature == "Profile") {
                val intent = Intent(this, Profile::class.java)
                intent.putExtra("Activity", feature)
                startActivity(intent)
                overridePendingTransition(R.anim.fade_in,0)
            } else if (feature == "Syllabus") {
                val intent = Intent(this, Syllabus::class.java)
                intent.putExtra("Activity", feature)
                startActivity(intent)
                overridePendingTransition(R.anim.fade_in,0)
            } else if (feature == "Time_table") {
                val intent = Intent(this, Time_table::class.java)
                intent.putExtra("Activity", feature)
                startActivity(intent)
                overridePendingTransition(R.anim.fade_in,0)
            }else if (feature == "Holiday") {
                val intent = Intent(this, Holidays::class.java)
                intent.putExtra("Activity", feature)
                startActivity(intent)
                overridePendingTransition(R.anim.fade_in,0)
            }else{
                Log.i("Clicked","Not found feature")
            }
        }
    }

//    private fun get_school_name_class() {
//
//        databaseReference.addListenerForSingleValueEvent(object :
//            ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//
//                School_name = dataSnapshot.child("School_ids").child(School_Id).getValue(String::class.java).toString()
//                Class = "Class "+dataSnapshot.child("Users").child(School_Id).child(User_Id).child("Class").getValue(String::class.java).toString()
//                val Class1 = dataSnapshot.child("Users").child(School_Id).child(User_Id).child("Class1").getValue(String::class.java).toString()
//
//                database_internal_2.putString("School_name", School_name)
//                database_internal_2.putString("Class", Class)
//                database_internal_2.putString("Class1", Class1)
//                database_internal_2.apply()
//
//            }
//            override fun onCancelled(databaseError: DatabaseError) {
//                Log.i("Message","DataBase Error")
//            }
//        })
//
//    }

    private fun get_subjects() {

        Log.i("Entered","Get Subject")

        databaseReference.child("Schools").child(School_name).child(Class).child("Subjects").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (childSnapshot in dataSnapshot.children) {

                    val total = dataSnapshot.childrenCount.toString()
                    val key = childSnapshot.key.toString()

                    val drawableName = key.decapitalize()+"_1"
                    Log.i("subject_name",drawableName)// Replace "your_drawable_name" with the name of your drawable file without extension
                    val resourceId = getDrawableResourceId(this@dashboard, drawableName)

                    dataclass_subject_list.add(dataclass_subject(key,resourceId))
                    //load_image_subjects(key.toString(),childSnapshot.value.toString(),total)
                    Log.i("Subjectssss",dataclass_subject_list.toString())

                    if(total == dataclass_subject_list.size.toString()){
                        Log.i("Subjects",dataclass_subject_list.toString())
                        a1 = adapter_subject(dataclass_subject_list)
                        r1.layoutManager = LinearLayoutManager(this@dashboard, LinearLayoutManager.HORIZONTAL, false)
                        r1.adapter = a1

                        on_click_subject()
                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
                println("Error fetching data: ${databaseError.message}")
            }

        })

    }

//    private fun load_image_subjects(key: String,image_string: String, total: Int) {
//
//        val storage = FirebaseStorage.getInstance()
//        val storageRef = storage.reference
//
//        val localFile = File.createTempFile("images", "jpg")
//        val imageRef = storageRef.child(image_string)
//
//        // Download image to local file
//        imageRef.getFile(localFile).addOnSuccessListener {
//            // Image downloaded successfully
//            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
//            dataclass_subject_list.add(dataclass_subject(key,bitmap))
//
//            //Log.i("Message",dataclass_subject_list.toString())
//
//            if(dataclass_subject_list.size == total){
//
//
//
//            }
//
//            // Now you can use 'imageData' wherever you need the image resource ID
//        }.addOnFailureListener { exception ->
//            exception.printStackTrace()
//        }
//    }

    private fun on_click_subject() {

        a1.setOnItemClickListener { position ->

            val string = dataclass_subject_list[position].title

            val intent = Intent(this, Subject_syllabus::class.java)
            intent.putExtra("Subject", string)
            startActivity(intent)
            overridePendingTransition(R.anim.fade_in,0)

        }
    }


    private fun update_name() {

        School_Id = database_internal.getString("School_Id", "1").toString()
        Phone_no = database_internal.getString("Phone_no", "1").toString()
        User_Id = database_internal.getString("User_Id", "1").toString()
        User_name = database_internal.getString("User_name", "Unknown").toString()
        School_name = database_internal.getString("School_name", "1").toString()
        Class ="Class "+ database_internal.getString("Class1", "1").toString()

        Log.i("Intialiaze","Intialize")
        Log.i("School_id", School_Id)
        Log.i("Phone_no", Phone_no)
        Log.i("User_id", User_Id)
        Log.i("User_name", User_name)
        Log.i("School_id", Class)
        Log.i("school_name", School_name)

        Name.setText(User_name)
        navi_name.setText(User_name)


    }

    private fun navigation_control() {

        val toggle = ActionBarDrawerToggle(this,drawer_layout,toolbar,R.string.navigation_open,R.string.navigation_close)

        toggle.setDrawerIndicatorEnabled(false);
        toggle.setHomeAsUpIndicator(R.drawable.ham2);
        toggle.setToolbarNavigationClickListener {
            if (drawer_layout.isDrawerVisible(GravityCompat.START)) {
                drawer_layout.closeDrawer(GravityCompat.START)
            } else {
                drawer_layout.openDrawer(GravityCompat.START)
            }
        }

        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        navi_layout.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {

                R.id.home -> {
                    startActivity(Intent(this, dashboard::class.java))
                    true
                }
                R.id.log_out -> {

                    //auth.signOut()
                    database_internal_2.putBoolean("Logged_value",false)
                    database_internal_2.apply()
                    startActivity(Intent(this, Login_page::class.java))
                    finish()
                     // Finish LoginActivity to prevent user from navigating back to it
                    true
                }
                R.id.profile ->{

                    startActivity(Intent(this, Profile::class.java))
                    true
                }
                else -> false

            }
        }

    }

    private fun fetch_datails() {

        Phone_no = database_internal.getString("Phone_no", "1").toString()

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value2 =
                    dataSnapshot.child("User_ids_pno").child(Phone_no).getValue(String::class.java).toString()
                val value = dataSnapshot.child("Exits").child(Phone_no).getValue(String::class.java).toString()
                val value3 =
                    dataSnapshot.child("Users").child("Parents").child(value.toString()).child(value2.toString())
                        .child("Name").getValue(String::class.java).toString()
                val address =
                    dataSnapshot.child("Users").child("Parents").child(value.toString()).child(value2.toString())
                        .child("Address").getValue(String::class.java).toString()
                val father_name =
                    dataSnapshot.child("Users").child("Parents").child(value.toString()).child(value2.toString())
                        .child("Father Name").getValue(String::class.java).toString()
                val mother_name =
                    dataSnapshot.child("Users").child("Parents").child(value.toString()).child(value2.toString())
                        .child("Mother Name").getValue(String::class.java).toString()
                val email =
                    dataSnapshot.child("Users").child("Parents").child(value.toString()).child(value2.toString())
                        .child("Email").getValue(String::class.java).toString()
                val password =
                    dataSnapshot.child("Users").child("Parents").child(value.toString()).child(value2.toString())
                        .child("Password").getValue(String::class.java).toString()
                val Class1 =
                    dataSnapshot.child("Users").child("Parents").child(value.toString()).child(value2.toString())
                        .child("Class").getValue(String::class.java).toString()
                val school_name =
                    dataSnapshot.child("Users").child("Parents").child(value.toString()).child(value2.toString())
                        .child("School Name").getValue(String::class.java).toString()


                database_internal_2.putString("School_Id", value.toString())
                database_internal_2.putString("Phone_no", Phone_no)
                database_internal_2.putString("User_Id", value2.toString())
                database_internal_2.putString("User_name", value3.toString())
                database_internal_2.putString("Class1", Class1)
                database_internal_2.putString("Address", address)
                database_internal_2.putString("Father_name", father_name)
                database_internal_2.putString("Mother_name", mother_name)
                database_internal_2.putString("Email", email)
                database_internal_2.putString("Password", password)
                database_internal_2.putString("School_name", school_name)
                database_internal_2.apply()

                Log.i("Class",Class1.toString())

            }


            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })
    }
    private fun isLogg_in(): Boolean {
        return database_internal.getBoolean("Logged_value",false)
    }


    fun getDrawableResourceId(context: Context, drawableName: String): Int {
        return context.resources.getIdentifier(drawableName, "drawable", context.packageName)
    }

    private fun Intialize() {


        //image_slider = findViewById<ImageSlider>(R.id.image_slider)
        School_logo = findViewById(R.id.dashboard_logo1)
        drawer_layout = findViewById<DrawerLayout>(R.id.drawer_l1)
        navi_layout = findViewById<NavigationView>(R.id.navi_v1)
        val headerView =navi_layout.getHeaderView(0)
        navi_name = headerView.findViewById(R.id.navi_student_name)
        navi_profile_image = headerView.findViewById(R.id.navi_profile_image)
        toolbar = findViewById<Toolbar>(R.id.toolbar_1)
        r1 = findViewById<RecyclerView>(R.id.r1)
        r2 = findViewById<RecyclerView>(R.id.r2)
        Name = findViewById(R.id.name_text)
        image = findViewById(R.id.dashboard_logo1)
        shimmer_1 = findViewById(R.id.shimmer_1)
        shimmer_2 = findViewById(R.id.shimmer_2)
        Shimmer_layout = findViewById(R.id.shimmer_layout)

        shimmer_1.startShimmer()
        shimmer_2.startShimmer()


    }

}