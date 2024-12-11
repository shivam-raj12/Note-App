package com.shivam_raj.noteapp.screens.noteListScreen.utils

import com.shivam_raj.noteapp.database.Note

fun List<Note>.filterByString(string: String): List<Note> {
    return this.filter {
        if (string.isEmpty()) {
            true
        } else if (string.isNotEmpty()) {
            (((it.fakeTitle ?: it.noteTitle)
                .startsWith(
                    prefix = string,
                    ignoreCase = true
                )) ||
                    ((it.fakeDescription ?: it.noteDescription).startsWith(
                        prefix = string,
                        ignoreCase = true
                    )))
        } else {
            false
        }
    }
}