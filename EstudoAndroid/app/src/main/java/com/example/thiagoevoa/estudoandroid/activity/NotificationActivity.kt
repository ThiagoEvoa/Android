package com.example.thiagoevoa.estudoandroid.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.thiagoevoa.estudoandroid.R
import com.example.thiagoevoa.estudoandroid.util.NOTIFICATION_BODY
import com.example.thiagoevoa.estudoandroid.util.NOTIFICATION_TITLE
import kotlinx.android.synthetic.main.activity_notification.*

class NotificationActivity : AppCompatActivity() {
    var title: String? = null
    var body: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        title = intent.getStringExtra(NOTIFICATION_TITLE)
        body = intent.getStringExtra(NOTIFICATION_BODY)

        initView()
    }

    private fun initView(){
        txt_title.text = title
        txt_body.text = body
    }
}
