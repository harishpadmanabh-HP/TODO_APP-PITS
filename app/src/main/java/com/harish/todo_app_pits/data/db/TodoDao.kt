package com.harish.todo_app_pits.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.harish.todo_app_pits.data.models.TODOItem

@Dao
interface TodoDao {

    @Query("SELECT * FROM tb_todo ORDER BY created_at ASC")
    fun getAllData(): LiveData<List<TODOItem>>

    @Query("SELECT * FROM tb_todo WHERE userid=:userId")
    fun getToDoByUserId(userId:Int): LiveData<List<TODOItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertData(toDoData: TODOItem)

    @Query("SELECT * FROM tb_todo WHERE id=:id")
    fun getTodoById(id:Int):LiveData<TODOItem>

    @Query("SELECT * FROM tb_todo WHERE title LIKE :searchQuery")
    fun searchDatabase(searchQuery: String): LiveData<List<TODOItem>>

    @Query("UPDATE tb_todo SET status=:status WHERE id=:id ")
    fun updateStatus(id:Int,status:Boolean)

    @Delete
     fun deleteItem(toDoData: TODOItem)




}