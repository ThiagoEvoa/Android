package com.example.thiagoevoa.estudoandroid.util

import android.content.Context
import android.content.Intent
import com.example.thiagoevoa.estudoandroid.R
import com.example.thiagoevoa.estudoandroid.activity.ScheduleActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

fun getFirebaseUser(auth: FirebaseAuth): FirebaseUser? {
    return auth.currentUser
}

fun login(context: Context, auth: FirebaseAuth, email: String, password: String) {
    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
        if (task.isSuccessful) {
            createSharedPreference(context, task.result?.user?.uid.toString())
            context.startActivity(Intent(context, ScheduleActivity::class.java))
        } else {
            showToast(context, context.resources.getString(R.string.error_login))
        }
    }
}

fun createAccount(context: Context, auth: FirebaseAuth, email: String, password: String) : Boolean{
    var result = false
    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
        if (task.isSuccessful) {
            showToast(context, context.resources.getString(R.string.success_create_user))
        } else {
            showToast(context, context.resources.getString(R.string.error_create_user))
        }
        result = task.isSuccessful
    }
    return result
}

fun updateEmail(context: Context, user: FirebaseUser, email: String) {
    user.updateEmail(email).addOnCompleteListener { task ->
        if (task.isSuccessful) {
            showToast(context, context.resources.getString(R.string.success_update_email))
        } else {
            showToast(context, context.resources.getString(R.string.error_update_email))
        }
    }
}

fun updatePassword(context: Context, user: FirebaseUser, password: String) {
    user.updatePassword(password).addOnCompleteListener { task ->
        if (task.isSuccessful) {
            showToast(context, context.resources.getString(R.string.success_update_password))
        } else {
            showToast(context, context.resources.getString(R.string.error_update_password))
        }
    }
}

fun deleteAccount(context: Context, user: FirebaseUser) {
    user.delete().addOnCompleteListener { task ->
        if (task.isSuccessful) {
            showToast(context, context.resources.getString(R.string.success_delete_user))
        } else {
            showToast(context, context.resources.getString(R.string.error_delete_user))
        }
    }
}

fun resetPassword(context: Context, auth: FirebaseAuth, password: String) {
    auth.sendPasswordResetEmail(password).addOnCompleteListener { task ->
        if (task.isSuccessful) {
            showToast(context, context.resources.getString(R.string.success_send_email))
        } else {
            showToast(context, context.resources.getString(R.string.error_send_email))
        }
    }
}

