package com.harish.todo_app_pits

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_todo_row_layout.view.*

class TodoListAdapter(val listener: TodoListener) : ListAdapter<TODOItem,TodoListAdapter.Viewholder>(TodoDiffUtil){
    class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_todo_row_layout, parent, false)
        return Viewholder(view)
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        val item = getItem(position)
        holder.itemView.apply {
            title_textview.text = "${item.title}\n${item.desc}"
            handleStatus(item.completed,status_textview)
            setOnClickListener {
                listener.onTodoItemClicked(item.id)
            }
        }

    }

    private fun handleStatus(completed: Boolean, statusTextview: TextView) {
        if(completed){
            statusTextview.apply {
                text = "Completed"
                setBackgroundResource(R.drawable.completed_bg)
            }
        }else{
            statusTextview.apply {
                text = "Pending"
                setBackgroundResource(R.drawable.pending_bg)
            }
        }

    }

}

object TodoDiffUtil:DiffUtil.ItemCallback<TODOItem>(){
    override fun areItemsTheSame(oldItem: TODOItem, newItem: TODOItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: TODOItem, newItem: TODOItem): Boolean {
        return oldItem.id == newItem.id
    }

}

interface TodoListener{
fun onTodoItemClicked(id:Int)
}