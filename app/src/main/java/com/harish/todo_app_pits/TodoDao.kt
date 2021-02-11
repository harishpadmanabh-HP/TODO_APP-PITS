package com.harish.todo_app_pits

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TodoDao {

    @Query("SELECT * FROM tb_todo ORDER BY id DESC")
    fun getAllData(): LiveData<List<TODOItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertData(toDoData: TODOItem)



}