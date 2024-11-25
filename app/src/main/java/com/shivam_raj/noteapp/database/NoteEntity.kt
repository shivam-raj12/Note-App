package com.shivam_raj.noteapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "Note_Table")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val noteTitle: String,
    val noteDescription: String,
    val fakeTitle: String? = null,
    val fakeDescription: String? = null,
    val notePriority: Int,
    val dateAdded: Long,
    val lastUpdate: Long,
    val pinnedAt: Long? = null,
    val password: String? = null,
    val colorIndex: Int? = null
) : Serializable {
    override fun toString(): String {
        return "Title: $noteTitle\n" +
                "Description: $noteDescription\n" +
                "Fake Title: $fakeTitle\n" +
                "Fake Description: $fakeDescription\n" +
                "Priority: $notePriority\n" +
                "Note Background Color: $colorIndex\n" +
                "Password: $password"
    }

}

val EmptyNote = Note(
    noteTitle = "Oops! Something unexpected happens:(",
    noteDescription = "We apologize for the inconvenience, but something unexpected occurred while displaying the note.\n\nWe recommend closing the screen (pressing the back button) and reopening the note.",
    notePriority = 0,
    dateAdded = System.currentTimeMillis(),
    lastUpdate = System.currentTimeMillis()
)