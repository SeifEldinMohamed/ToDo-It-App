package com.seif.todoit.ui.veiwmodels

import android.app.Application
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.seif.todoit.R
import com.seif.todoit.data.models.PriorityModel
import com.seif.todoit.data.models.TodoModel

class ShareViewModel(application: Application) : AndroidViewModel(application) {
    var emptyDataBase: MutableLiveData<Boolean> = MutableLiveData(true)
    fun checkDatabaseEmpty(todoData: List<TodoModel>) {
        emptyDataBase.value = todoData.isEmpty()
    }

    val listener: AdapterView.OnItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            when (position) {
                0 -> (parent?.getChildAt(0) as TextView).setTextColor(
                    ContextCompat.getColor(
                        application,
                        R.color.red
                    )
                )
                1 -> (parent?.getChildAt(0) as TextView).setTextColor(
                    ContextCompat.getColor(
                        application,
                        R.color.yellow
                    )
                )
                2 -> (parent?.getChildAt(0) as TextView).setTextColor(
                    ContextCompat.getColor(
                        application,
                        R.color.green
                    )
                )
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {

        }
    }

    fun getPriority(priority: String): PriorityModel {
        return when (priority) {
            "Low Priority" -> PriorityModel.LOW
            "Medium Priority" -> PriorityModel.MEDIUM
            "High Priority" -> PriorityModel.HIGH
            else -> PriorityModel.HIGH
        }
    }

    fun validateTodo(todoTitle: String, todoDescription: String): Boolean {
        return (todoTitle.isNotEmpty() || todoDescription.isNotEmpty())
    }

    fun parsePriorityToInt(priorityModel: PriorityModel): Int {
        return when (priorityModel) {
            PriorityModel.HIGH -> 0
            PriorityModel.MEDIUM -> 1
            PriorityModel.LOW -> 2
        }
    }
}