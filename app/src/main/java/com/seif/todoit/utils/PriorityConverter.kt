package com.seif.todoit.utils

import androidx.room.TypeConverter
import com.seif.todoit.data.models.PriorityModel

class PriorityConverter {
    @TypeConverter // let room knows that it will use type converter
    fun fromPriorityToString(priorityModel:PriorityModel):String{
        return priorityModel.name
    }
    @TypeConverter
    fun  fromStringToPriority(priorityModel:String):PriorityModel{
        return PriorityModel.valueOf(priorityModel)
    }
}