package com.example.thiagoevoa.estudoandroid.dao

import android.arch.persistence.room.*
import com.example.thiagoevoa.estudoandroid.model.Tool

@Dao
interface ToolDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(tool: Tool): Long

    @Update
    fun update(vararg tool: Tool): Int

    @Delete
    fun delete(vararg tool: Tool): Int

    @Query("SELECT * FROM tb_tool")
    fun listAll(): MutableList<Tool>

    @Query("SELECT * FROM tb_tool WHERE id = :id")
    fun selectById(id: Long) : Tool?

    @Query("SELECT * FROM tb_tool WHERE description LIKE :description ORDER BY description")
    fun selectByDescription(description: String = "%"): MutableList<Tool>
}