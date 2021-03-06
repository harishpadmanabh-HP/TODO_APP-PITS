package com.harish.todo_app_pits.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.harish.todo_app_pits.*
import com.harish.todo_app_pits.adapters.TodoListAdapter
import com.harish.todo_app_pits.adapters.TodoListener
import com.harish.todo_app_pits.data.models.TODOItem
import com.harish.todo_app_pits.ui.CreateTodo
import com.harish.todo_app_pits.ui.TodoDetails
import com.harish.todo_app_pits.viewmodels.TodoViewModel
import com.harish.todo_app_pits.viewmodels.TodoViewModelFactory
import kotlinx.android.synthetic.main.fragment_all_todos.view.*
import kotlinx.android.synthetic.main.fragment_all_todos.view.compose_fab
import kotlinx.android.synthetic.main.fragment_all_todos.view.todo_list
import kotlinx.android.synthetic.main.fragment_your_todo.view.*

class AllTodosFragment : Fragment(),
    TodoListener,SearchView.OnQueryTextListener {

    private lateinit var root: View
    private lateinit var viewModel: TodoViewModel
    private val adapter by lazy {
        TodoListAdapter(
            this
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewmodel()
        setHasOptionsMenu(true)


    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_all_todos, container, false)
        viewModel.fetchTodos()
        root.compose_fab.setOnClickListener {
            startActivity(Intent(requireContext(),
                CreateTodo::class.java))
        }


        return root

    }

    override fun onResume() {
        super.onResume()
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.apply {

            allTodosFromDb.observe(requireActivity(), Observer {
                if(it.isNullOrEmpty())
                    viewModel.fetchTodos()
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
            root.todo_list.smoothScrollToPosition(0)

        }

    }

    private fun initViewmodel() {
        val viewModelFactory =
            TodoViewModelFactory(
                requireActivity().application
            )
        viewModel =
            ViewModelProvider(requireActivity(), viewModelFactory).get(TodoViewModel::class.java)

    }

    override fun onTodoItemClicked(item: TODOItem) {
        startActivity(Intent(requireContext(),
            TodoDetails::class.java)
            .putExtra("id",item.id)
            .putExtra("item", Gson().toJson(item))
        )

    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.topbar_menu, menu)

        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {

        if (query != null && query.isNotEmpty()) {
            val searchQuery = "%$query%"

            viewModel.searchDatabase(searchQuery).observe(requireActivity(), Observer {
                 setupRecyclerView(it)
            })
        }

        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if (query != null && query.isNotEmpty()) {
            val searchQuery = "%$query%"

            viewModel.searchDatabase(searchQuery).observe(requireActivity(), Observer {
                setupRecyclerView(it)
            })
        }

        return true
    }


}