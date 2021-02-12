package com.harish.todo_app_pits.data.models


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "tb_todo")
    data class TODOItem(

        @ColumnInfo(name = "status")
        @SerializedName("completed")
        val completed: Boolean,

        @PrimaryKey(autoGenerate = false)
        @ColumnInfo(name = "id")
        @SerializedName("id")
        val id: Int,

        @SerializedName("title")
        @ColumnInfo(name = "title")
        val title: String,

        @ColumnInfo(name = "userid")
        @SerializedName("userId")
        val userId: Int,

        @ColumnInfo(name = "created_at")
        var createdAt: Long,

        @ColumnInfo(name = "desc")
        var desc: String = " "
        )
