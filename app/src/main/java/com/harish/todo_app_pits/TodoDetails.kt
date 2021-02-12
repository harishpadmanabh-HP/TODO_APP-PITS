package com.harish.todo_app_pits

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_todo_details.*

class TodoDetails : AppCompatActivity() {
    private lateinit var viewModel: TodoViewModel
    val id by lazy { intent.getIntExtra("id",-1) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_details)
        initViewmodel()

    }
    private fun initViewmodel() {
        val viewModelFactory = TodoViewModelFactory(application)
        viewModel =  ViewModelProvider(this, viewModelFactory).get(TodoViewModel::class.java)

    }

    override fun onResume() {
        super.onResume()
        setupObservers()
    }

    private fun setupObservers() {
        if(id > -1)
            viewModel.getTodoById(id).observe(this, Observer {
             renderDetails(it)
            })
    }

    private fun renderDetails(todo: TODOItem?) {
        todo?.let {todo->
            title_textview.text = todo.title
            userid_textview.text = "User : ${todo.userId}"
            status_textview.text = if(todo.completed) "Status : Completed" else  "Status : Pending"
            handleDescriptionTextView(todo.desc)
            handleCompleteButtonVisibility(todo.completed)


        }

    }

    private fun handleDescriptionTextView(desc: String) {
      if(desc.isNullOrEmpty()){
          desc_textview.text = "Description : $desc"
      }else{
          desc_textview.visibility = View.GONE
      }
    }

    private fun handleCompleteButtonVisibility(completed: Boolean) {
        if(!completed)
            completed_btn.visibility = View.VISIBLE
        else
            completed_btn.visibility = View.INVISIBLE



    }
}