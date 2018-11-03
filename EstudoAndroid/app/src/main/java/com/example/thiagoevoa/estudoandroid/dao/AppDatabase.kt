package com.example.thiagoevoa.estudoandroid.dao

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.example.thiagoevoa.estudoandroid.model.Tool

@Database(entities = [Tool::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun toolDAO(): ToolDAO
}