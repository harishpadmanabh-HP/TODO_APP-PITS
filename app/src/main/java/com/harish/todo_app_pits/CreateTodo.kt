package com.harish.todo_app_pits

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_create_todo.*

class CreateTodo : AppCompatActivity() {

    private lateinit var viewModel: TodoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_todo)
        initViewmodel()
    }
    private fun initViewmodel() {
        val viewModelFactory = TodoViewModelFactory(application)
        viewModel =  ViewModelProvider(this, viewModelFactory).get(TodoViewModel::class.java)

    }

    fun onCreateToDoClicked(view: View) {
        val title = title_edittext.text.toString()
        val desc = desc_edittext.text.toString()//optional

        val newItem = TODOItem(
            false,
            (55..100).random(),
            title,
            (0..10).random()
            )

        if(title.isNotEmpty()){
            viewModel.insertData(newItem)
            Toast.makeText(this, "Todo Created !!!", Toast.LENGTH_SHORT).show()
            //onBackPressed()
        }else
        {
            Toast.makeText(this, "Title cannot be empty", Toast.LENGTH_SHORT).show()
        }


    }
}