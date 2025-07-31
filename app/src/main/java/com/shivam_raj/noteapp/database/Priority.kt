package com.shivam_raj.noteapp.database

import androidx.room.TypeConverter

enum class Priority(
    val id: Int,
) {
    HIGH(3),
    MEDIUM(2),
    LOW(1),
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
            3 -> Priority.HIGH
            2 -> Priority.MEDIUM
            1 -> Priority.LOW
            else -> Priority.NONE
        }
    }
}