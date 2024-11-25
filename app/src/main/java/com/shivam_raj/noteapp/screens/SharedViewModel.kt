package com.shivam_raj.noteapp.screens

import androidx.lifecycle.ViewModel
import com.shivam_raj.noteapp.database.Note
import com.shivam_raj.noteapp.screens.noteSecurityScreen.SecurityData

class SharedViewModel : ViewModel() {
    var selectedNote:Note? = null

    var securityData = SecurityData(
        password = selectedNote?.password,
        fakeTitle = selectedNote?.fakeTitle,
        fakeDescription = selectedNote?.fakeDescription
    )
}