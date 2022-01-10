package com.seif.todoit.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.seif.todoit.R
import com.seif.todoit.data.models.PriorityModel
import com.seif.todoit.data.models.TodoModel
import com.seif.todoit.databinding.TodoRowDesignBinding
import com.seif.todoit.ui.fragments.ToDoListFragmentDirections

class TodoListAdapter() : RecyclerView.Adapter<TodoListAdapter.MyViewHolder>() {
    var todoList = emptyList<TodoModel>()

    class MyViewHolder(val binding: TodoRowDesignBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            TodoRowDesignBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.todoTitleTxt.text = todoList[position].title
        holder.binding.todoDescriptionTxt.text = todoList[position].description

        holder.binding.todoItemCons.setOnClickListener {
            val action =
                ToDoListFragmentDirections.actionToDoListFragmentToUpdateTodoFragment(todoList[position])
            holder.itemView.findNavController().navigate(action)
        }

        when (todoList[position].priority) {
            PriorityModel.HIGH -> holder.binding.priorityIndicator.setCardBackgroundColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.red
                )
            )
            PriorityModel.MEDIUM -> holder.binding.priorityIndicator.setCardBackgroundColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.yellow
                )
            )
            PriorityModel.LOW -> holder.binding.priorityIndicator.setCardBackgroundColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.green
                )
            )
        }
    }

    override fun getItemCount(): Int {
        return todoList.size
    }

    fun setData(todoList2: List<TodoModel>) {
        this.todoList = todoList2
        notifyDataSetChanged()
    }
}
