package com.example.thiagoevoa.estudoandroid.activity

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.example.thiagoevoa.estudoandroid.R
import com.example.thiagoevoa.estudoandroid.fragment.ProfessionalDetailFragment
import com.example.thiagoevoa.estudoandroid.model.Professional
import com.example.thiagoevoa.estudoandroid.util.EXTRA_PROFESSIONAL
import com.example.thiagoevoa.estudoandroid.util.PROFESSIONAL_DETAIL_FRAGMENT
import kotlinx.android.synthetic.main.activity_professional_detail.*
import kotlinx.android.synthetic.main.app_bar_professional_detail.*

class ProfessionalDetailActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_professional_detail)
        setSupportActionBar(toolbar_professional_detail)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar_professional_detail, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        val professional = intent?.getParcelableExtra(EXTRA_PROFESSIONAL) as Professional?
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.detail_professional, ProfessionalDetailFragment().newInstance(professional), PROFESSIONAL_DETAIL_FRAGMENT).commit()
    }

    override fun onResume() {
        super.onResume()
        nav_view.menu.findItem(R.id.nav_professional).isChecked = true
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
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

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
