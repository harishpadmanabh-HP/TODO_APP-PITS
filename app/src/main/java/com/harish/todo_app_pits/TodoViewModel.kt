package com.harish.todo_app_pits

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TodoViewModel(val app: Application) : AndroidViewModel(app){

    private val todoDao = TodoDatabase.getDatabase(app).toDoDao()
    private val todoRepository = TodoRepository(app)
    val events = MutableLiveData<String>()
    val allTodosFromDb: LiveData<List<TODOItem>> = todoDao.getAllData()


    fun fetchTodos()=todoRepository.getTodosFromServer(onApiCallback = {
        status, message, response ->
        if(status){
            if (response != null) {

                        for(item in response){
                           // todoDao.insertData(item)
                            insertData(item)
                        }




            }else{
                events.postValue("No Todos Found")
            }
        }else{
            events.postValue(message)
        }
    })

    fun insertData(toDoData: TODOItem) {
        Log.e("ROOM vm","DataInserted ${toDoData.title}       ${toDoData.id}")

        viewModelScope.launch(Dispatchers.IO) {
            todoDao.insertData(toDoData.also {
                it.createdAt = System.currentTimeMillis()
            })
            Log.e("ROOM","DataInserted ${toDoData.title}")
        }
    }






}

open class TodoViewModelFactory(val app: Application) : ViewModelProvider.AndroidViewModelFactory(app){

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TodoViewModel(app) as T
    }
}