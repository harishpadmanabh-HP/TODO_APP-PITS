package com.harish.todo_app_pits.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.harish.todo_app_pits.R
import com.harish.todo_app_pits.viewmodels.TodoViewModel
import com.harish.todo_app_pits.viewmodels.TodoViewModelFactory
import com.harish.todo_app_pits.data.models.TODOItem
import com.harish.todo_app_pits.utils.UserUtils
import kotlinx.android.synthetic.main.activity_create_todo.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import kotlin.coroutines.CoroutineContext

class CreateTodo() : AppCompatActivity(),CoroutineScope {

    private lateinit var viewModel: TodoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_todo)
        initViewmodel()
    }
    private fun initViewmodel() {
        val viewModelFactory =
            TodoViewModelFactory(application)
        viewModel =  ViewModelProvider(this, viewModelFactory).get(TodoViewModel::class.java)

    }

    fun onCreateToDoClicked(view: View) {
        val title = title_edittext.text.toString()
        val desc = desc_edittext.text.toString()//optional
        val id = UserUtils.init(this).getLocalUserId()

        val newItem = TODOItem(
            false,
            (300..500).random(),
            title,
            id!!,
            System.currentTimeMillis(),
            desc

        )

        if(title.isNotEmpty()){
          async {
              viewModel.insertData(newItem)

              withContext(Main){
                  Toast.makeText(this@CreateTodo, "Todo Created !!!", Toast.LENGTH_SHORT).show()
                  startActivity(Intent(this@CreateTodo,
                      MainActivity::class.java))
              }
          }
//            viewModel.insertData(newItem)
//            Toast.makeText(this, "Todo Created !!!", Toast.LENGTH_SHORT).show()
            //onBackPressed()
        }else
        {
            Toast.makeText(this, "Title cannot be empty", Toast.LENGTH_SHORT).show()
        }


    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO+ SupervisorJob()

    override fun onDestroy() {
        coroutineContext.cancelChildren()
        super.onDestroy()

    }
}