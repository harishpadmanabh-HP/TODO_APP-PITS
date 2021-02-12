package com.harish.todo_app_pits

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TodoDao {

    @Query("SELECT * FROM tb_todo ORDER BY created_at DESC")
    fun getAllData(): LiveData<List<TODOItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertData(toDoData: TODOItem)

    @Query("SELECT * FROM tb_todo WHERE id=:id")
    fun getTodoById(id:Int):LiveData<TODOItem>

    @Query("SELECT * FROM tb_todo WHERE title LIKE :searchQuery")
    fun searchDatabase(searchQuery: String): LiveData<List<TODOItem>>




}