package com.seif.todoit.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.seif.todoit.data.models.TodoModel

@Dao
interface TodoDao {
    @Query("SELECT * FROM todo_table ORDER BY id ASC")
    fun getAllToDos(): LiveData<List<TodoModel>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTodo(todo: TodoModel)

    @Update
    suspend fun updateTodo(todo: TodoModel)

    @Delete
    suspend fun deleteTodo(todo: TodoModel)

    @Query("DELETE FROM todo_table")
    suspend fun deleteAllToDos()

    @Query("SELECT * FROM todo_table WHERE title LIKE :searchQuery")
    fun searchTodo(searchQuery: String): LiveData<List<TodoModel>>

    // this 1 means that I want to return rows satisfy this condition first
    @Query("SELECT * FROM todo_table ORDER BY CASE WHEN priority LIKE 'H%' THEN 1 WHEN priority LIKE 'M%' THEN 2 WHEN priority LIKE 'L%' THEN 3 END")
    fun sortByPriorityHigh(): LiveData<List<TodoModel>>

    @Query("SELECT * FROM todo_table ORDER BY CASE WHEN priority LIKE 'L%' THEN 1 WHEN priority LIKE 'M%' THEN 2 WHEN priority LIKE 'H%' THEN 3 END")
    fun sortByPriorityLow(): LiveData<List<TodoModel>>
}