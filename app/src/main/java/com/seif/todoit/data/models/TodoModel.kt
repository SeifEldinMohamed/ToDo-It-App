package com.seif.todoit.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "todo_table")
@Parcelize
data class TodoModel(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var title: String,
    var priority: PriorityModel,
    var description: String
) : Parcelable