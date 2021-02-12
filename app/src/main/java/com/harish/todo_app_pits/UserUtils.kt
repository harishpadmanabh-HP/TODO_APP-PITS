package com.harish.todo_app_pits

import android.content.Context
import android.content.SharedPreferences

object UserUtils {
    private var preferences: SharedPreferences? = null

    fun init(context: Context): UserUtils {
        preferences = context.getSharedPreferences("user_utils", Context.MODE_PRIVATE)
        return this
    }

    fun setLocalUserId(id:Int){
        val editor = preferences?.edit()
        editor?.putInt("userId",id)
        editor?.apply()

    }
    fun getLocalUserId():Int?{
        var id:Int= -1
        try{
            id= preferences?.getInt("userId",-1)!!

        }catch (e:Exception){

        }
        return id
    }

}