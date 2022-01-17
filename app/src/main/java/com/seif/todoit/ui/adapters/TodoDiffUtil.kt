package com.seif.todoit.ui.adapters

import androidx.recyclerview.widget.DiffUtil
import com.seif.todoit.data.models.TodoModel

class TodoDiffUtil(
    private val oldList: List<TodoModel>,
    private val newList: List<TodoModel>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun areContentsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int
    ): Boolean { // compares content of the two lists to each other
        return oldList[oldItemPosition].id == newList[newItemPosition].id
                && oldList[oldItemPosition].title == newList[newItemPosition].title
                && oldList[oldItemPosition].description == newList[newItemPosition].description
                && oldList[oldItemPosition].priority == newList[newItemPosition].priority
        // equals to oldList[oldItemPosition] == newList[newItemPosition]
    } // called when areItemsTheSame fun return true
}
