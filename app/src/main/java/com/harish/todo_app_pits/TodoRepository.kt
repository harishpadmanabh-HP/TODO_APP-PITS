package com.harish.todo_app_pits

import android.content.Context
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TodoRepository(val context: Context) {

    private val api = Apis()
    private val todoDao = TodoDatabase.getDatabase(context).toDoDao()


    fun getTodosFromServer(onApiCallback: (status: Boolean, message: String?, response: List<TODOItem>?) -> Unit) {
        api.getTodosFromServer().enqueue(object : Callback<List<TODOItem>> {
            override fun onFailure(call: Call<List<TODOItem>>, t: Throwable) {
                if (!Utils.hasInternet(context))
                    onApiCallback(false, "No internet", null)
                else
                    onApiCallback(false, "Something went wrong", null)
            }

            override fun onResponse(
                call: Call<List<TODOItem>>,
                response: Response<List<TODOItem>>
            ) {
                if (response.isSuccessful)
                    onApiCallback(true, null, response.body())
                else
                    onApiCallback(false, "Something went wrong", null)


            }
        })
    }

     fun insertData(toDoData: TODOItem){
        todoDao.insertData(toDoData)
    }

    fun getTodoById(id:Int)=
        todoDao.getTodoById(id)

    fun searchDatabase(searchQuery: String): LiveData<List<TODOItem>> {
        return todoDao.searchDatabase(searchQuery)
    }



}