package com.example.thiagoevoa.estudoandroid.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "tb_tool",
        indices = [Index("description", unique = true)])
data class Tool(@PrimaryKey(autoGenerate = true) var id: Long?,
                @ColumnInfo(name = "description") var description: String,
                var active: Boolean)