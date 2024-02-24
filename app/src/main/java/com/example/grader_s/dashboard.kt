package com.example.grader_s

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView

class dashboard : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val drawer_layout = findViewById<DrawerLayout>(R.id.drawer_l1)
        val navi_layout = findViewById<NavigationView>(R.id.navi_v1)
        val toolbar = findViewById<Toolbar>(R.id.toolbar_1)


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

        val r1 = findViewById<RecyclerView>(R.id.r1)

        val i1 = listOf(
            dataclass_subject("Item 1", R.drawable.evaluation),
            dataclass_subject("Item 2", R.drawable.logo_sc),
            dataclass_subject("Item 3", R.drawable.logo_sc),
            dataclass_subject("Item 4", R.drawable.book),
            dataclass_subject("Item 5", R.drawable.logo_sc),
            dataclass_subject("Item 6", R.drawable.logo_sc),
            dataclass_subject("Item 7", R.drawable.logo_sc),
            dataclass_subject("Item 8", R.drawable.logo_sc),
            // Add more items as needed
        )

        val a1 = adapter_subject(i1)
        r1.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        r1.adapter = a1

        val r2 = findViewById<RecyclerView>(R.id.r2)

        val i2 = listOf(
            dataclass_subject("Attendence", R.drawable.attendence_icon),
            dataclass_subject("Notification", R.drawable.notification_icon1),
            dataclass_subject("Assignment", R.drawable.assignment_icon),
            dataclass_subject("Syallabus", R.drawable.syallabus_icon),
            dataclass_subject("Profile", R.drawable.profile_icon),
            dataclass_subject("Time Table", R.drawable.book),
            dataclass_subject("Holidays", R.drawable.evaluation),
            dataclass_subject("Exam Result", R.drawable.book),
            // Add more items as needed
        )

        a1.setOnItemClickListener { position ->

            val intent = Intent(this, Subject_syllabus::class.java)
            intent.putExtra("EXTRA_DATA", position.toString())
            startActivity(intent)
            overridePendingTransition(R.anim.fade_in,0)


        }


        val a2 = adapter_features(i2)
        r2.layoutManager = GridLayoutManager(this,2)
        r2.adapter = a2

        a2.setOnItemClickListener { position ->

            if (position == 0){
                startActivity(Intent(this, Attendence::class.java))
                overridePendingTransition(R.anim.fade_in,0)
            }else if (position == 1){
                startActivity(Intent(this, Notification::class.java))
                overridePendingTransition(R.anim.fade_in,0)
            }else if (position == 2){
                startActivity(Intent(this, Assignment::class.java))
                overridePendingTransition(R.anim.fade_in,0)
            }else if (position == 3){
//                startActivity(Intent(this, Assignment::class.java))
//                overridePendingTransition(R.anim.fade_in,0)
            }else if (position == 4){
                startActivity(Intent(this, Profile::class.java))
                overridePendingTransition(R.anim.fade_in,0)
            }else if (position == 5){
//                startActivity(Intent(this, Assignment::class.java))
//                overridePendingTransition(R.anim.fade_in,0)
            }else if (position == 6){
//                startActivity(Intent(this, Assignment::class.java))
//                overridePendingTransition(R.anim.fade_in,0)
            }else if (position == 7){
//                startActivity(Intent(this, Assignment::class.java))
//                overridePendingTransition(R.anim.fade_in,0)
            }



        }
    }
}