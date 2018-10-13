package com.example.thiagoevoa.estudoandroid.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.thiagoevoa.estudoandroid.R
import com.example.thiagoevoa.estudoandroid.util.createAccount
import com.example.thiagoevoa.estudoandroid.util.login
import com.example.thiagoevoa.estudoandroid.util.resetPassword
import com.example.thiagoevoa.estudoandroid.util.showToast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        initViews()
    }

    override fun onResume() {
        super.onResume()
        clearFields()
    }

    private fun initViews() {
        btn_login.setOnClickListener {
            when {
                edt_email.text.toString().isEmpty() -> {
                    showToast(this, resources.getString(R.string.error_edt_email))
                }
                edt_password.text.toString().isEmpty() -> {
                    showToast(this, resources.getString(R.string.error_edt_password))
                }
                else -> {
                   login(this, auth!!, edt_email.text.toString(), edt_password.text.toString())
                }
            }
        }

        txt_reset_password.setOnClickListener {
            when{
                edt_password.text.toString().isEmpty() -> {
                    showToast(this, resources.getString(R.string.error_edt_password))
                }
                else ->{
                    resetPassword(this, auth!!, edt_password.text.toString())
                }
            }
        }

        txt_create_account.setOnClickListener {
            when {
                edt_email.text.toString().isEmpty() -> {
                    showToast(this, resources.getString(R.string.error_edt_email))
                }
                edt_password.text.toString().isEmpty() -> {
                    showToast(this, resources.getString(R.string.error_edt_password))
                }
                else -> {
                    createAccount(this, auth!!, edt_email.text.toString(), edt_password.text.toString())
                }
            }
        }
    }

    private fun clearFields() {
        edt_email.text.clear()
        edt_password.text.clear()
        edt_email.isFocusable = true
    }
}
