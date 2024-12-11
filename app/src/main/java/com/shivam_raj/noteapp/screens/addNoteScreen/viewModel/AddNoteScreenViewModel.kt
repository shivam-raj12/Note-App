package com.shivam_raj.noteapp.screens.addNoteScreen.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shivam_raj.noteapp.database.Note
import com.shivam_raj.noteapp.database.NoteRepository
import com.shivam_raj.noteapp.database.Priority
import com.shivam_raj.noteapp.screens.noteSecurityScreen.SecurityData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddNoteScreenViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
) : ViewModel() {
    val title = mutableStateOf("")

    val description = mutableStateOf("")

    val securityData = mutableStateOf(SecurityData())

    fun updateNoteData(title: String?, description: String?){
        title?.let {
            this.title.value = title
        }
        description?.let {
            this.description.value = description
        }
    }

    fun updateSecurityData(securityData: SecurityData){
        this.securityData.value = securityData
    }

    fun setTitle(title: String) {
        this.title.value = title
    }

    fun setDescription(description: String) {
        this.description.value = description
    }

    fun onSaveNoteButtonClick(
        note: Note?,
        priority: Priority,
        colorIndex: Int?
    ) {
        val updatedNote = note?.copy(
            noteTitle = title.value,
            noteDescription = description.value,
            notePriority = priority,
            fakeTitle = securityData.value.fakeTitle,
            fakeDescription = securityData.value.fakeDescription,
            password = securityData.value.password,
            colorIndex = colorIndex,
            lastUpdate = System.currentTimeMillis()
        ) ?: Note(
            noteTitle = title.value,
            noteDescription = description.value,
            notePriority = priority,
            fakeTitle = securityData.value.fakeTitle,
            fakeDescription = securityData.value.fakeDescription,
            password = securityData.value.password,
            colorIndex = colorIndex,
            lastUpdate = System.currentTimeMillis(),
            dateAdded = System.currentTimeMillis()
        )
        if (note == null || note.toString() != updatedNote.toString()) {
            viewModelScope.launch {
                noteRepository.addOrUpdateNote(updatedNote)
            }
        }
    }

}