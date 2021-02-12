package com.harish.todo_app_pits.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.harish.todo_app_pits.data.db.TodoDatabase
import com.harish.todo_app_pits.data.models.TODOItem
import com.harish.todo_app_pits.data.repository.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class TodoViewModel(val app: Application) : AndroidViewModel(app){

    private val todoDao = TodoDatabase.getDatabase(app).toDoDao()
    private val todoRepository =
        TodoRepository(app)
    val events = MutableLiveData<String>()
    val allTodosFromDb: LiveData<List<TODOItem>> = todoDao.getAllData()

    fun fetchTodos()=todoRepository.getTodosFromServer(onApiCallback = {
        status, message, response ->
        if(status){
            if (response != null) {

                        for(item in response){
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
            todoDao.insertData(toDoData.also {
                it.createdAt = System.currentTimeMillis()
                it.desc= if(!toDoData.desc.isNullOrEmpty())
                          toDoData.desc
                else
                    " "
            })
        }
    }

    fun getTodoById(id:Int):LiveData<TODOItem> = todoRepository.getTodoById(id)

    fun searchDatabase(searchQuery: String): LiveData<List<TODOItem>>{
        return todoRepository.searchDatabase(searchQuery)
    }

    fun getTodoByUserId(id:Int): LiveData<List<TODOItem>> = todoRepository.getTodoByUserId(id)

    fun updateStatus(id: Int,status:Boolean){
        viewModelScope.launch (IO){
            todoRepository.updateStatus(id,status)
        }
    }

    fun deleteTodo(toDoData: TODOItem){
        viewModelScope.launch (IO){
            todoRepository.deleteTodo(toDoData)
        }
    }













}

open class TodoViewModelFactory(val app: Application) : ViewModelProvider.AndroidViewModelFactory(app){

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TodoViewModel(app) as T
    }
}