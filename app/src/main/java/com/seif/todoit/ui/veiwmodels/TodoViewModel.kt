package com.seif.todoit.ui.veiwmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.seif.todoit.data.local.TodoDatabase
import com.seif.todoit.data.models.TodoModel
import com.seif.todoit.data.repository.RepositoryImplementation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TodoViewModel(application: Application) : AndroidViewModel(application) {
    private val todoDao = TodoDatabase.getInstance(application).todoDao()
    private val repository: RepositoryImplementation = RepositoryImplementation(todoDao)
    val getAllToDos: LiveData<List<TodoModel>> = todoDao.getAllToDos()

    fun insertTodo(todoModel: TodoModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addTodo(todoModel)
        }
    }
    fun updateTodo(todoModel: TodoModel){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTodo(todoModel)
        }
    }
    fun deleteTodo(todoModel: TodoModel){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteTodo(todoModel)
        }
    }

}