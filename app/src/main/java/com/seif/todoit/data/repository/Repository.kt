package com.seif.todoit.data.repository

import androidx.lifecycle.LiveData
import com.seif.todoit.data.models.TodoModel

interface Repository {
    fun getAllToDos(): LiveData<List<TodoModel>>
    suspend fun addTodo(todo:TodoModel)
}