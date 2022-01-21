package com.seif.todoit.data.repository

import androidx.lifecycle.LiveData
import com.seif.todoit.data.models.TodoModel

interface Repository {
    fun getAllToDos(): LiveData<List<TodoModel>>
    suspend fun addTodo(todo: TodoModel)
    suspend fun updateTodo(todo: TodoModel)
    suspend fun deleteTodo(todo: TodoModel)
    suspend fun deleteAllToDos()
    fun searchTodo(searchQuery: String):LiveData<List<TodoModel>>
    fun sortByPriorityHigh():LiveData<List<TodoModel>>
}