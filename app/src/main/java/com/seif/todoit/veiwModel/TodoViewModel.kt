package com.seif.todoit.veiwModel

import android.app.Application
import android.content.Context
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.seif.todoit.data.local.TodoDatabase
import com.seif.todoit.data.models.TodoModel
import com.seif.todoit.data.repository.RepositoryImplementation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import smartdevelop.ir.eram.showcaseviewlib.GuideView
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType
import smartdevelop.ir.eram.showcaseviewlib.listener.GuideListener

class TodoViewModel(application: Application) : AndroidViewModel(application) {
    private val todoDao = TodoDatabase.getInstance(application).todoDao()
    private val repository: RepositoryImplementation = RepositoryImplementation(todoDao)
    val getAllToDos: LiveData<List<TodoModel>> = todoDao.getAllToDos()

    fun insertTodo(todoModel: TodoModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addTodo(todoModel)
        }
    }

    fun updateTodo(todoModel: TodoModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTodo(todoModel)
        }
    }

    fun deleteTodo(todoModel: TodoModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteTodo(todoModel)
        }
    }

    fun deleteAllToDos() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllToDos()
        }
    }

    fun searchTodo(searchQuery: String):LiveData<List<TodoModel>>{
       return repository.searchTodo(searchQuery)
    }

    fun sortByPriorityHigh():LiveData<List<TodoModel>>{
        return repository.sortByPriorityHigh()
    }

    fun sortByPriorityLow():LiveData<List<TodoModel>>{
        return repository.sortByPriorityLow()
    }
//    fun showCaseAddNewTodo(view: View,view2: View,view3: View, context: Context){
//        GuideView.Builder(context)
//            .setTitle("Add new note")
//            .setContentText("Click here to navigate for new note page so you can add new note")
//            .setDismissType(DismissType.anywhere) //optional - default DismissType.targetView
//            .setTargetView(view)
//            .setContentTextSize(14)//optional
//            .setTitleTextSize(16)//optional
//            .setGuideListener {
//                showCaseSearch(view2,view3,context)
//            }
//            .build()
//            .show()
//    }
//    private fun showCaseSearch(view: View, view2: View, context: Context){
//        GuideView.Builder(context)
//            .setTitle("Search for note")
//            .setContentText("Click here to navigate for new note page so you can add new note")
//            .setDismissType(DismissType.anywhere) //optional - default DismissType.targetView
//            .setTargetView(view)
//            .setContentTextSize(12)//optional
//            .setTitleTextSize(14)//optional
//            .setGuideListener{
//                showCaseDarkMode(view2,context)
//            }
//            .build()
//            .show()
//    }
//    private fun showCaseDarkMode(view: View, context: Context){
//        GuideView.Builder(context)
//            .setTitle("Dark Mode")
//            .setContentText("Click here to activate Dark mode")
//            .setDismissType(DismissType.anywhere) //optional - default DismissType.targetView
//            .setTargetView(view)
//            .setContentTextSize(12)//optional
//            .setTitleTextSize(14)//optional
//            .build()
//            .show()
//    }
}