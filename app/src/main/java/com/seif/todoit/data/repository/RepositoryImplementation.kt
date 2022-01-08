package com.seif.todoit.data.repository

import androidx.lifecycle.LiveData
import com.seif.todoit.data.local.TodoDao
import com.seif.todoit.data.models.TodoModel

class RepositoryImplementation(private val todoDao: TodoDao) :Repository {
    override fun getAllToDos(): LiveData<List<TodoModel>> {
        return todoDao.getAllToDos()
    }

    override suspend fun addTodo(todo: TodoModel) {
        todoDao.addTodo(todo)
    }
}