package com.shivam_raj.noteapp.screens.noteListScreen.viewModel

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shivam_raj.noteapp.database.Note
import com.shivam_raj.noteapp.database.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * View model class for [NoteHomeScreen](com.shivam_raj.noteapp.screens.noteListScreen.NoteHomeScreen).
 *
 * @param noteRepository An instance of [NoteRepository] class. It requires for doing database operations like fetching or deleting the note etc.
 *
 * @property noteList List of all the saved notes.
 * @property pinIcon Whether to show pinned icon or unpin icon when multiple notes are selected.
 * @property pinnedNotesCount Counting all the pinned notes item in [selectedNotes]
 * @property searchBarTextValue Text shown in search field. This value is used to filter notes.
 * @property isSelectionModeActive Whether to show action mode or normal topBar. This value is true when note's selection mode is active.
 * @property selectedNotes List of all selected notes.
 * @property showDeleteConfirmationDialog Decides whether to show [DeleteConfirmationDialog](com.shivam_raj.noteapp.screens.noteListScreen.utils.DeleteConfirmationDialog) or not. This value is true when the delete icon button in action mode is clicked.
 * @property snackBarHostState Used to display snackBar.
 * @property showSetPasswordDialogBox Decides whether to show [SetPasswordDialog](com.shivam_raj.noteapp.screens.noteListScreen.utils.SetPasswordDialog) or not. This value is true when the lock icon button in action mode is clicked.
 * @property addOrUpdateNote Adds or update note to the room database
 * @property deleteNote Deletes the note from room database
 */
@HiltViewModel
class NoteListScreenViewModel @Inject constructor(private val noteRepository: NoteRepository) :
    ViewModel() {

    val noteList: Flow<List<Note>> = noteRepository.getAllNote()

    /**
     * This variable decides whether to show pin icon or unpin icon in the top bar when multiple notes are selected.
     *  - Default value is true, it means the topBar in action mode will show pin icon.
     *  - This value changes changes to false if all the [selectedNotes] are already pinned. If any single note is not pinned in the [selectedNotes] list, then it will remains to true.
     *
     * We are counting all the pinned note in [selectedNotes] list using [pinnedNotesCount] variable.
     */
    private var _pinIcon by mutableStateOf(true)
    val pinIcon by derivedStateOf { _pinIcon }
    private var pinnedNotesCount by mutableIntStateOf(0)

    private var _searchBarTextValue by mutableStateOf("")
    val searchBarTextValue by derivedStateOf { _searchBarTextValue }

    private var _isSelectionModeActive by mutableStateOf(false)
    val isSelectionModeActive by derivedStateOf { _isSelectionModeActive }

    var selectedNotes: SnapshotStateList<Note> = mutableStateListOf()
        private set

    var showDeleteConfirmationDialog by mutableStateOf(false)
        private set

    val snackBarHostState = SnackbarHostState()

    var showSetPasswordDialogBox by mutableStateOf(false)
        private set

    private fun addOrUpdateNote(note: Note) {
        viewModelScope.launch {
            noteRepository.addOrUpdateNote(note)
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            noteRepository.deleteNote(note)
        }
    }

    fun updateSearchBarText(value: String) {
        _searchBarTextValue = value
    }

    fun onNoteLongClick(note: Note) {
        if (!_isSelectionModeActive) reverseSelectionMode()
        if (selectedNotes.contains(note)) {
            selectedNotes.remove(note)
            if (selectedNotes.isEmpty()) {
                reverseSelectionMode()
                return
            }
            if (note.pinnedAt != null) pinnedNotesCount--
        } else {
            selectedNotes.add(0, note)
            if (note.pinnedAt != null) pinnedNotesCount++
        }
        _pinIcon = pinnedNotesCount != selectedNotes.size
    }

    fun onPinIconClick() {
        selectedNotes.forEachIndexed { index, note ->
            if (_pinIcon) {
                if (note.pinnedAt == null) addOrUpdateNote(
                    note.copy(pinnedAt = System.currentTimeMillis() + index)
                )
            } else {
                addOrUpdateNote(
                    note.copy(pinnedAt = null)
                )
            }
        }
        reverseSelectionMode()
    }

    fun addNoteToSelectedNoteList(note: Note) {
        if (!selectedNotes.contains(note)) {
            selectedNotes.add(note)
            if (note.pinnedAt != null) pinnedNotesCount++
        }
        _pinIcon = pinnedNotesCount != selectedNotes.size
    }

    fun deleteAllSelectedNote() {
        selectedNotes.forEach { note ->
            if (note.password == null) {
                deleteNote(note)
            } else {
                if (snackBarHostState.currentSnackbarData == null) {
                    viewModelScope.launch {
                        snackBarHostState.showSnackbar("Password protected note can't be deleted. Please delete them manually.")
                    }
                }
            }
        }
        reverseSelectionMode()
        if (showDeleteConfirmationDialog) showDeleteConfirmationDialog = false
    }

    fun reverseSelectionMode() {
        _isSelectionModeActive = !_isSelectionModeActive
        if (!_isSelectionModeActive) selectedNotes.clear(); _pinIcon = true; pinnedNotesCount = 0
    }


    fun onDeleteIconClick() {
        showDeleteConfirmationDialog = !showDeleteConfirmationDialog
    }

    fun onLockIconClick() {
        showSetPasswordDialogBox = !showSetPasswordDialogBox
    }

    fun setPasswordForAllSelectedNote(password: String) {
        selectedNotes.forEach {
            viewModelScope.launch {
                if (it.password == null) {
                    noteRepository.updatePasswordForNoteID(it.id, password)
                } else {
                    if (snackBarHostState.currentSnackbarData == null) {
                        snackBarHostState.showSnackbar("Notes that are already password protected remains unchanged.")
                    }
                }
            }
        }
        reverseSelectionMode()
        onLockIconClick()
    }
}

