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
val CONTENT_TYPE_JSON = MediaType.parse("application/json; charset=utf-8")
const val CONTENT_TYPE_TEXT_PLAIN = "text/plain"

//SharedPreferences
const val SHARED_PREFERENCES = "sharedPreference"
const val TOKEN = "token"

//Response Code
const val RESPONSE_OK = 200

//Firebase Messaging
const val NOTIFICATION_CHANNEL_ID = "notification_id"
const val NOTIFICATION_NAME = "notification"
const val NOTIFICATION_CONTENT_INFO = "info"
const val NOTIFICATION_TITLE = "title"
const val NOTIFICATION_BODY = "body"
const val DEFAULT_TOPIC = "all"

//Permissions
const val REQUEST_PERMISSION = 1