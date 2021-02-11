package com.harish.todo_app_pits

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_all_todos.*
import kotlinx.android.synthetic.main.fragment_all_todos.view.*

class AllTodosFragment : Fragment(),TodoListener {

    private lateinit var root: View
    private lateinit var viewModel: TodoViewModel
    private val adapter by lazy { TodoListAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewmodel()


    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_all_todos, container, false)
        viewModel.fetchTodos()
        setupObservers()
        root.compose_fab.setOnClickListener {
            startActivity(Intent(requireContext(),CreateTodo::class.java))
        }


        return root

    }

    private fun setupObservers() {
        viewModel.apply {

            allTodosFromDb.observe(requireActivity(), Observer {
                setupRecyclerView(it)
            })

            events.observe(requireActivity(), Observer {
                Snackbar.make(root,it,Snackbar.LENGTH_LONG).show()
            })
        }
    }

    private fun setupRecyclerView(todos: List<TODOItem>?) {

        if(todos.isNullOrEmpty()){
           // Snackbar.make((android.R.id.content),"NO Todos Found",Snackbar.LENGTH_LONG).show()
        }else{
            adapter.submitList(todos)
            root.todo_list.adapter = adapter
        }

    }

    private fun initViewmodel() {
        val viewModelFactory = TodoViewModelFactory(requireActivity().application)
        viewModel =
            ViewModelProvider(requireActivity(), viewModelFactory).get(TodoViewModel::class.java)

    }


}