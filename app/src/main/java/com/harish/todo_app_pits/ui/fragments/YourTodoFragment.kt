package com.harish.todo_app_pits.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.harish.todo_app_pits.R
import com.harish.todo_app_pits.viewmodels.TodoViewModel
import com.harish.todo_app_pits.adapters.TodoListAdapter
import com.harish.todo_app_pits.adapters.TodoListener
import com.harish.todo_app_pits.data.models.TODOItem
import com.harish.todo_app_pits.ui.CreateTodo
import com.harish.todo_app_pits.ui.TodoDetails
import com.harish.todo_app_pits.utils.UserUtils
import kotlinx.android.synthetic.main.fragment_your_todo.view.*

class YourTodoFragment : Fragment(),
    TodoListener, SearchView.OnQueryTextListener {

    private val viewModel: TodoViewModel by viewModels()
    private val adapter by lazy {
        TodoListAdapter(
            this
        )
    }
    private lateinit var root: View
    private  var userID:Int? = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
       userID = UserUtils.init(requireContext()).getLocalUserId()


    }

    override fun onResume() {
        super.onResume()
        setupObservers()
    }

    private fun setupObservers(){
        viewModel.apply {
            if (userID != null) {
                getTodoByUserId(userID!!).observe(requireActivity(), Observer {
                    setupRecyclerView(it)
                })
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_your_todo, container, false)
        root.compose_fab.setOnClickListener {
            startActivity(Intent(requireContext(),
                CreateTodo::class.java))
        }
        return root
    }

    override fun onTodoItemClicked(item: TODOItem) {
        startActivity(Intent(requireContext(),
            TodoDetails::class.java)
            .putExtra("id",item.id)
            .putExtra("item", Gson().toJson(item))
        )

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

    private fun setupRecyclerView(todos: List<TODOItem>?) {

        if (todos.isNullOrEmpty()) {
            handleEmpty()
        } else {
            root.load_layout.visibility = View.GONE
            adapter.submitList(todos)
            root.todo_list.adapter = adapter
            root.todo_list.smoothScrollToPosition(0)
        }

    }

    private fun handleEmpty() {
        root.load_layout.visibility = View.VISIBLE
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.topbar_menu, menu)

        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
    }


}