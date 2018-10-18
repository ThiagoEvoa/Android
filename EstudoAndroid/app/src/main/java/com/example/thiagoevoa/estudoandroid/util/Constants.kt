package com.example.thiagoevoa.estudoandroid.util

import okhttp3.MediaType

//URL`s
const val HOST = "https://study-node-mongodb.herokuapp.com/api/"
const val URL_SCHEDULE = "${HOST}schedule"
const val URL_CLIENT = "${HOST}client"
const val URL_PROFESSIONAL = "${HOST}professional"

//Fragment Tag
const val SCHEDULE_DETAIL_FRAGMENT = "scheduleDetailFragment"
const val CLIENT_DETAIL_FRAGMENT = "clientDetailFragment"
const val PROFESSIONAL_DETAIL_FRAGMENT = "professionalDetailFragment"

//Extra Intent
const val EXTRA_SCHEDULE = "extraSchedule"
const val EXTRA_CLIENT = "extraClient"
const val EXTRA_PROFESSIONAL = "extraProfessional"

//Bundle
const val BUNDLE_POSITION = "position"

//ContentType
val CONTENT_TYPE = MediaType.parse("application/json; charset=utf-8")

//SharedPreferences
const val SHARED_PREFERENCES = "sharedPreference"
const val TOKEN = "token"

//Response Code

const val RESPONSE_OK = 200
