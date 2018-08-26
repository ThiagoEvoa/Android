package com.example.thiagoevoa.estudoandroid.activity

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
import kotlinx.android.synthetic.main.activity_schedule_detail.*
import kotlinx.android.synthetic.main.app_bar_schedule_detail.*

class ScheduleDetailActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_detail)
        setSupportActionBar(toolbar_schedule_detail)

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

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.menu, menu)
//        menu.findItem(R.id.action_save).isVisible = true
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            R.id.action_save ->{
//                Toast.makeText(this, "Action Save", Toast.LENGTH_LONG).show()
//                true
//            }
//            R.id.action_delete-> {
//                Toast.makeText(this, "Action Delete", Toast.LENGTH_LONG).show()
//                true
//            }
//            R.id.action_search-> {
//                Toast.makeText(this, "Action Search", Toast.LENGTH_LONG).show()
//                true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_schedule -> {

            }
            R.id.nav_professional -> {

            }
            R.id.nav_client -> {

            }
            R.id.nav_tools -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_logout -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
