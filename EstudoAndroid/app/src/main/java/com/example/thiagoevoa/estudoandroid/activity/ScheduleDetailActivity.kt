package com.example.thiagoevoa.estudoandroid.activity

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.example.thiagoevoa.estudoandroid.R
import com.example.thiagoevoa.estudoandroid.fragment.ScheduleDetailFragment
import com.example.thiagoevoa.estudoandroid.model.Schedule
import com.example.thiagoevoa.estudoandroid.util.EXTRA_SCHEDULE
import com.example.thiagoevoa.estudoandroid.util.SCHEDULE_DETAIL_FRAGMENT
import com.example.thiagoevoa.estudoandroid.util.logout
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_schedule_detail.*
import kotlinx.android.synthetic.main.app_bar_schedule_detail.*

class ScheduleDetailActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_detail)
        setSupportActionBar(toolbar_schedule_detail)

        auth = FirebaseAuth.getInstance()

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar_schedule_detail, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        val schedule = intent?.getParcelableExtra(EXTRA_SCHEDULE) as Schedule?
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.detail_schedule, ScheduleDetailFragment().newInstance(schedule), SCHEDULE_DETAIL_FRAGMENT).commit()
    }

    override fun onResume() {
        super.onResume()
        nav_view.menu.findItem(R.id.nav_schedule).isChecked = true
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_schedule -> {
                startActivity(Intent(this, ScheduleActivity::class.java))
                finish()
            }
            R.id.nav_professional -> {
                startActivity(Intent(this, ProfessionalActivity::class.java))
                finish()
            }
            R.id.nav_client -> {
                startActivity(Intent(this, ClientActivity::class.java))
                finish()
            }
            R.id.nav_tools -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_logout -> {
                logout(this, auth!!)
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
