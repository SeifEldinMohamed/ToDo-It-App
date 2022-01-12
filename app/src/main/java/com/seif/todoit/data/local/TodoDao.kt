package com.seif.todoit.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.seif.todoit.data.models.TodoModel

@Dao
interface TodoDao {
    @Query("SELECT * FROM todo_table ORDER BY id ASC")
    fun getAllToDos():LiveData<List<TodoModel>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTodo(todo:TodoModel)

    @Update
    suspend fun updateTodo(todo: TodoModel)

    @Delete
    suspend fun deleteTodo(todo: TodoModel)

    @Query("DELETE FROM todo_table")
    suspend fun deleteAllToDos()
}