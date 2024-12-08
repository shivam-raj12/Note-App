package com.shivam_raj.noteapp.database

import androidx.room.TypeConverter

enum class Priority(
    val id: Int,
) {
    HIGH(4),
    MEDIUM(3),
    LOW(2),
    VERY_LOW(1),
    NONE(0)
}

class TypeConverter{
    @TypeConverter
    fun fromPriority(priority: Priority): Int{
        return priority.id
    }

    @TypeConverter
    fun toPriority(id: Int): Priority{
        return when(id) {
            4 -> Priority.HIGH
            3 -> Priority.MEDIUM
            2 -> Priority.LOW
            1 -> Priority.VERY_LOW
            else -> Priority.NONE
        }
    }
}