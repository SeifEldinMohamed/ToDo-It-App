package com.seif.todoit.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.seif.todoit.data.models.TodoModel
import com.seif.todoit.utils.PriorityConverter

private const val DATABASE_NAME = "todo_table"
@Database(entities = [TodoModel::class], version = 1, exportSchema = false)
@TypeConverters(PriorityConverter::class)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao

    companion object {
        @Volatile
        private var instance: TodoDatabase? = null
        fun getInstance(context:Context): TodoDatabase {
            if (instance != null) {
                return instance!!
            }
                return synchronized(this){ // to prevent any thread to deal with it until this thread unlock it
                    Room.databaseBuilder(
                        context,
                        TodoDatabase::class.java,
                        DATABASE_NAME
                    ).build()
                }
        }
    }
}