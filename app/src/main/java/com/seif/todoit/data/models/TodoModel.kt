package com.seif.todoit.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo_table")
data class TodoModel (
    @PrimaryKey(autoGenerate = true)
    var id:Int,
    var title:String,
    var priority:PriorityModel,
    var description:String
)