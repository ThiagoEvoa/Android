package com.example.thiagoevoa.estudoandroid.util

import android.content.Context
import android.widget.Toast

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
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