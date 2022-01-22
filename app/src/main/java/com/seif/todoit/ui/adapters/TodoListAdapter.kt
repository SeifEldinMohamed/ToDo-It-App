package com.seif.todoit.ui.adapters

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.seif.todoit.R
import com.seif.todoit.data.models.PriorityModel
import com.seif.todoit.data.models.TodoModel
import com.seif.todoit.databinding.TodoRowDesignBinding
import com.seif.todoit.ui.fragments.ToDoListFragmentDirections
import com.seif.todoit.utils.TodoDiffUtil

class TodoListAdapter() : RecyclerView.Adapter<TodoListAdapter.MyViewHolder>() {
    var todoList = emptyList<TodoModel>()
    private var isNightMode:Boolean = false
    private lateinit var settingPref: SharedPreferences
    class MyViewHolder(val binding: TodoRowDesignBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int, todoList: List<TodoModel>) {
            binding.todoTitleTxt.text = todoList[position].title
            binding.todoDescriptionTxt.text = todoList[position].description

            binding.todoItemCons.setOnClickListener {
                val action = ToDoListFragmentDirections
                    .actionToDoListFragmentToUpdateTodoFragment(todoList[position])
                itemView.findNavController().navigate(action)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            TodoRowDesignBinding.inflate
                (
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(position, todoList)
        setSpinnerPriorityColor(holder, position)
    }

    override fun getItemCount(): Int {
        return todoList.size
    }

    fun setData(newTodoList: List<TodoModel>) {
        val diffUtilCallback = TodoDiffUtil(todoList, newTodoList)
        val diffUtilResult = DiffUtil.calculateDiff(diffUtilCallback)
        this.todoList = newTodoList
        diffUtilResult.dispatchUpdatesTo(this)

    }
    // used instead of setData to prevent recycler view from scrolling to the bottom after sorting
    fun setSortedData(newTodoList: List<TodoModel>) {
        todoList = newTodoList
        notifyDataSetChanged() // rebind and relayout all views
    }

    private fun setSpinnerPriorityColor(holder: MyViewHolder, position: Int) {
        when (todoList[position].priority) {
            PriorityModel.HIGH -> {
                holder.binding.priorityIndicator.setCardBackgroundColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.priorityCircleRed
                    )
                )
                // to change background of to-do according to theme

                if(isDarkMode(holder)){
                    holder.binding.todoItemCons.setBackgroundResource(
                        R.drawable.todo_backg_red
                    )
                }
                else{
                    holder.binding.todoItemCons.setBackgroundResource(
                        R.drawable.todo_backg
                    )
                }
            }
            PriorityModel.MEDIUM -> {
                holder.binding.priorityIndicator.setCardBackgroundColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.priorityCircleYellow
                    )
                )
                if(isDarkMode(holder)){
                    holder.binding.todoItemCons.setBackgroundResource(
                        R.drawable.todo_backg_yellow
                    )
                }
                else{
                    holder.binding.todoItemCons.setBackgroundResource(
                        R.drawable.todo_backg
                    )
                }

            }
            PriorityModel.LOW -> {
                holder.binding.priorityIndicator.setCardBackgroundColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.priorityCircleGreen
                    )
                )
                if(isDarkMode(holder)){
                    holder.binding.todoItemCons.setBackgroundResource(
                        R.drawable.todo_backg_green
                    )
                }
                else{
                    holder.binding.todoItemCons.setBackgroundResource(
                        R.drawable.todo_backg
                    )
                }
            }
        }
    }
    private fun isDarkMode(holder: MyViewHolder):Boolean{
        settingPref  = holder.itemView.context.getSharedPreferences("settingPrefs", Context.MODE_PRIVATE)
        isNightMode = settingPref.getBoolean("nightMode",false)
        return isNightMode
    }
}
/**
 *  notifyDataSetChanged()
 *  it's problem that it's assume that all existing items and structure may no longer be valid.
 *  LayoutManagers will be forced to fully rebind and relayout all visible views.
 *  if you are writing an adapter it will always be more efficient to use the more specific
 *  change events if you can. Rely on notifyDataSetChanged() as a last resort.
 * */