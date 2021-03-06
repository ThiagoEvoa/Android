package com.example.thiagoevoa.estudoandroid.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.example.thiagoevoa.estudoandroid.R
import com.example.thiagoevoa.estudoandroid.activity.LoginActivity
import com.example.thiagoevoa.estudoandroid.model.Client
import com.example.thiagoevoa.estudoandroid.model.Professional
import com.example.thiagoevoa.estudoandroid.model.Schedule
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject

fun fullScreen(activity: Activity) {
    activity.requestWindowFeature(Window.FEATURE_NO_TITLE)
    activity.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
    activity.window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
}

fun logout(activity: Activity, auth: FirebaseAuth) {
    AlertDialog.Builder(activity).setTitle("Warning!").setMessage("Would you like to close application?")
            .setPositiveButton("yes") { dialog, which ->
                auth.signOut()
                deleteSharedPreference(activity)
                activity.finishAffinity()
                activity.startActivity(Intent(activity, LoginActivity::class.java))
            }
            .setNegativeButton("no") { dialog, which -> }
            .create().show()
}

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}

fun requestPermission(activity: Activity, permissions: Array<String>) {
    for (permission in permissions) {
        if (ContextCompat.checkSelfPermission(activity.baseContext, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, permissions, REQUEST_PERMISSION)
        }
    }
}

fun share(activity: Activity, subject: String, text: String) {
    activity.startActivity(Intent.createChooser(
            Intent(android.content.Intent.ACTION_SEND)
                    .setType(CONTENT_TYPE_TEXT_PLAIN)
                    .putExtra(android.content.Intent.EXTRA_SUBJECT, subject)
                    .putExtra(android.content.Intent.EXTRA_TEXT, text),
            activity.resources.getString(R.string.success_share)
    ))
}

fun createSharedPreference(context: Context, value: String) {
    val sharedPreference = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE).edit()
    sharedPreference.putString(TOKEN, value)
    sharedPreference.apply()
}

fun deleteSharedPreference(context: Context) {
    val sharedPreference = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE).edit()
    sharedPreference.putString(TOKEN, null)
    sharedPreference.apply()
}

fun isUserLogged(context: Context): Boolean {
    val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)
    return sharedPreferences.getString(TOKEN, null) != null
}

fun getClientFromJSON(value: String?): MutableList<Client> {
    val list: MutableList<Client> = mutableListOf()
    if (value != null) {
        val jsonArray = JSONArray(value)
        for (i in 0 until jsonArray.length()) {
            val item = Gson().fromJson(jsonArray[i].toString(), Client::class.java)
            list.add(item)
        }
    }
    return list
}

fun getProfessionalFromJSON(value: String?): MutableList<Professional> {
    val list: MutableList<Professional> = mutableListOf()
    if (value != null) {
        val jsonArray = JSONArray(value)
        for (i in 0 until jsonArray.length()) {
            val item = Gson().fromJson(jsonArray[i].toString(), Professional::class.java)
            list.add(item)
        }
    }
    return list
}

fun getScheduleFromJSON(value: String?): MutableList<Schedule> {
    val list: MutableList<Schedule> = mutableListOf()
    if (value != null) {
        val jsonArray = JSONArray(value)
        for (i in 0 until jsonArray.length()) {
            val item = Gson().fromJson(jsonArray[i].toString(), Schedule::class.java)
            list.add(item)
        }
    }
    return list
}

fun readErrorMessage(response: Response): MutableList<String> {
    return JSONObject(response.body()?.string()).getString("error").split(",") as MutableList<String>
}