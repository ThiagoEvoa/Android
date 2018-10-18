package com.example.thiagoevoa.estudoandroid.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.example.thiagoevoa.estudoandroid.R
import com.example.thiagoevoa.estudoandroid.util.fullScreen
import com.example.thiagoevoa.estudoandroid.util.isUserLogged

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fullScreen(this)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            val intent = if (isUserLogged(this)) {
                Intent(this, ScheduleActivity::class.java)
            } else {
                Intent(this, LoginActivity::class.java)
            }

            startActivity(intent)
            finish()
        }, 3000)
    }
}
