package com.example.thiagoevoa.estudoandroid.activity

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import com.example.thiagoevoa.estudoandroid.R
import com.example.thiagoevoa.estudoandroid.fragment.ScheduleListFragment
import com.example.thiagoevoa.estudoandroid.util.deleteSharedPreference
import com.example.thiagoevoa.estudoandroid.util.logout
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_schedule.*
import kotlinx.android.synthetic.main.app_bar_schedule.*

class ScheduleActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private var menuDelete: MenuItem? = null
    private var searchView: SearchView? = null
    private var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule)
        setSupportActionBar(toolbar_schedule)

        auth = FirebaseAuth.getInstance()

        btn_add_schedule.setOnClickListener { view ->
            startActivity(Intent(view.context, ScheduleDetailActivity::class.java))
        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar_schedule, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onResume() {
        super.onResume()
        nav_view.menu.findItem(R.id.nav_schedule).isChecked = true
    }

    override fun onBackPressed() {
        when {
            !searchView!!.isIconified || menuDelete!!.isVisible -> {
                (supportFragmentManager.fragments[0] as ScheduleListFragment).resetMenuIcons()
            }
            drawer_layout.isDrawerOpen(GravityCompat.START) -> {
                drawer_layout.closeDrawer(GravityCompat.START)
            }
            else -> {
                AlertDialog.Builder(this).setTitle("Warning!").setMessage("Would you like to close application?")
                        .setPositiveButton("yes") { dialog, which ->
                            super.onBackPressed()
                        }
                        .setNegativeButton("no") { dialog, which -> }
                        .create().show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        menuDelete = menu.findItem(R.id.action_delete)

        val menuItem = menu?.findItem(R.id.action_search)
        searchView = menuItem?.actionView as SearchView

        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_schedule -> {

            }
            R.id.nav_professional -> {
                startActivity(Intent(this, ProfessionalActivity::class.java))
            }
            R.id.nav_client -> {
                startActivity(Intent(this, ClientActivity::class.java))
            }
            R.id.nav_tools -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_logout -> {
                logout(auth!!)
                deleteSharedPreference(this)
                finish()
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
