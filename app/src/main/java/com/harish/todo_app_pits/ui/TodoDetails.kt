package com.harish.todo_app_pits.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.harish.todo_app_pits.R
import com.harish.todo_app_pits.viewmodels.TodoViewModel
import com.harish.todo_app_pits.viewmodels.TodoViewModelFactory
import com.harish.todo_app_pits.data.models.TODOItem
import kotlinx.android.synthetic.main.activity_todo_details.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlin.coroutines.CoroutineContext

class TodoDetails : AppCompatActivity() ,CoroutineScope {
    private lateinit var viewModel: TodoViewModel
    val id by lazy { intent.getIntExtra("id", -1) }
    val todoItem by lazy {
     Gson().fromJson( intent.getStringExtra("item"),
         TODOItem::class.java )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_details)
        initViewmodel()

    }

    private fun initViewmodel() {
        val viewModelFactory =
            TodoViewModelFactory(application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(TodoViewModel::class.java)

    }

    override fun onResume() {
        super.onResume()
        setupObservers()
        completed_btn.setOnClickListener {
           updateStatus()
        }
        delete_btn.setOnClickListener {
            deleteTodo()
        }
    }

    private fun deleteTodo() {
       async {
           withContext(IO){
               viewModel.deleteTodo(todoItem)
           }
           withContext(Main){
               startActivity(Intent(this@TodoDetails,
                   MainActivity::class.java))
           }
       }
    }

    private fun updateStatus() {
        async {
            withContext(IO){
                viewModel.updateStatus(id,true)
            }
            withContext(Main){
                startActivity(Intent(this@TodoDetails,
                    MainActivity::class.java))
            }
        }
    }

    private fun setupObservers() {
        if (id > -1)
            viewModel.getTodoById(id).observe(this, Observer {
                renderDetails(it)
            })
    }

    private fun renderDetails(todo: TODOItem?) {
        todo?.let { todo ->
            title_textview.text = todo.title
            userid_textview.text = "User : ${todo.userId}"
            status_textview.text = if (todo.completed) "Status : Completed" else "Status : Pending"
            handleDescriptionTextView(todo.desc)
            handleCompleteButtonVisibility(todo.completed)


        }

    }

    private fun handleDescriptionTextView(desc: String) {
        if (desc.isNullOrEmpty()) {
            desc_textview.text = "Description : $desc"
        } else {
            desc_textview.visibility = View.GONE
        }
    }

    private fun handleCompleteButtonVisibility(completed: Boolean) {
        if (!completed)
            completed_btn.visibility = View.VISIBLE
        else
            completed_btn.visibility = View.INVISIBLE
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + SupervisorJob()

    override fun onDestroy() {
        coroutineContext.cancelChildren()
        super.onDestroy()
    }
}