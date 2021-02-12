package com.harish.todo_app_pits.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.harish.todo_app_pits.data.models.TODOItem

@Database(entities = [TODOItem::class],version = 2,exportSchema = false)
abstract class TodoDatabase : RoomDatabase(){
    abstract fun toDoDao(): TodoDao
    companion object {
        @Volatile
        private var INSTANCE: TodoDatabase? = null

        fun getDatabase(context: Context): TodoDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(
                        context
                    )
                        .also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                TodoDatabase::class.java, "todo_database"
            ).build()
    }



}