package com.harish.todo_app_pits

import android.app.Application
import android.content.Context
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
        viewModelScope.launch(Dispatchers.IO) {
            todoDao.insertData(toDoData)
        }
    }






}

open class TodoViewModelFactory(val app: Application) : ViewModelProvider.AndroidViewModelFactory(app){

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TodoViewModel(app) as T
    }
}