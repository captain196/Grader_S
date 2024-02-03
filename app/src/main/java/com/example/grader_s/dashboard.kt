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

        val attendence = findViewById<AppCompatButton>(R.id.attendence_btn)
        attendence.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, Attendence::class.java))
            overridePendingTransition(R.anim.fade_in,0)
        })

        val assignment = findViewById<AppCompatButton>(R.id.assignment_btn)
        assignment.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, Assignment::class.java))
            overridePendingTransition(R.anim.fade_in,0)

        })

        val notification = findViewById<AppCompatButton>(R.id.notificatio_btn)
        notification.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, Notification::class.java))
            overridePendingTransition(R.anim.fade_in,0)
        })
    }
}