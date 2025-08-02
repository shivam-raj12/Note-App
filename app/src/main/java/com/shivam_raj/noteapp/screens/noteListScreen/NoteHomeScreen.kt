package com.shivam_raj.noteapp.screens.noteListScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.shivam_raj.noteapp.database.Note
import com.shivam_raj.noteapp.navigation.Screens
import com.shivam_raj.noteapp.screens.noteListScreen.utils.ActionModeIconsList
import com.shivam_raj.noteapp.screens.noteListScreen.utils.DeleteConfirmationDialog
import com.shivam_raj.noteapp.screens.noteListScreen.utils.EmptyNoteList
import com.shivam_raj.noteapp.screens.noteListScreen.utils.EmptySearchResult
import com.shivam_raj.noteapp.screens.noteListScreen.utils.NoteHomeScreenFloatingButton
import com.shivam_raj.noteapp.screens.noteListScreen.utils.SetPasswordDialog
import com.shivam_raj.noteapp.screens.noteListScreen.utils.filterByString
import com.shivam_raj.noteapp.screens.noteListScreen.viewModel.NoteListScreenViewModel

@Composable
fun NoteHomeScreen(
    noteListScreenViewModel: NoteListScreenViewModel = hiltViewModel(),
    navigator: NavHostController,
    onNoteClick: (Note) -> Unit,
    clearNote: () -> Unit
) {
    LaunchedEffect(Unit) {
        clearNote()
    }

    val navigateToAddNoteScreen: () -> Unit = { navigator.navigate(Screens.AddNoteScreen.route) }

    /**
     * Fetching all the saved notes and applying search field filter.
     *  - It will return null, if the data from room database are still loading
     *  - It will return empty list, if there is no any saved note
     */
    val noteList: List<Note>? =
        noteListScreenViewModel.noteList.collectAsStateWithLifecycle(initialValue = null).value?.filterByString(
            noteListScreenViewModel.searchBarTextValue
        )

    Scaffold(
        modifier = Modifier.imePadding(),
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
        snackbarHost = {
            SnackbarHost(
                hostState = noteListScreenViewModel.snackBarHostState
            )
        },
        floatingActionButton = {
            if (noteList != null) {
                NoteHomeScreenFloatingButton(
                    visibility = (noteList.isNotEmpty()) || (noteListScreenViewModel.searchBarTextValue.isNotEmpty()),
                    onClick = navigateToAddNoteScreen
                )
            }
        },
        topBar = {
            if (noteList != null) {
                if ((noteList.isNotEmpty()) || (noteListScreenViewModel.searchBarTextValue.isNotEmpty())) {
                    CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onBackground) {
                        NoteListTopBar(
                            actionModeText = noteListScreenViewModel.selectedNotes.size.toString(),
                            value = noteListScreenViewModel.searchBarTextValue,
                            onValueChange = noteListScreenViewModel::updateSearchBarText,
                            onClearFilterClicked = {
                                noteListScreenViewModel.updateSearchBarText("")
                            },
                            showAction = noteListScreenViewModel.isSelectionModeActive,
                            onCloseActionModeClick = {
                                noteListScreenViewModel.reverseSelectionMode()
                            },
                            content = {
                                ActionModeIconsList(
                                    showPinIcon = noteListScreenViewModel.pinIcon,
                                    onSelectAllIconClick = {
                                        noteList.forEach {
                                            noteListScreenViewModel.addNoteToSelectedNoteList(it)
                                        }
                                    },
                                    onPinUnpinIconClick = noteListScreenViewModel::onPinIconClick,
                                    onLockIconClick = noteListScreenViewModel::onLockIconClick,
                                    onDeleteIconClick = noteListScreenViewModel::onDeleteIconClick
                                )
                            }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        if (noteList != null) {
            if (noteList.isNotEmpty()) {
                NoteList(
                    NoteListData(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        noteList = noteList,
                        onDeleteNoteIconClick = noteListScreenViewModel::deleteNote,
                        onNoteLongClick = noteListScreenViewModel::onNoteLongClick,
                        onNoteClick = { note ->
                            if (!noteListScreenViewModel.isSelectionModeActive) {
                                onNoteClick(note)
                                if (note.password == null) {
                                    navigator.navigate(
                                        Screens.DetailScreen.route
                                    )
                                } else {
                                    navigator.navigate(
                                        Screens.PasswordScreen.route
                                    )
                                }
                            } else {
                                noteListScreenViewModel.onNoteLongClick(note)
                            }
                        },
                        selectedNoteList = if (noteListScreenViewModel.isSelectionModeActive) noteListScreenViewModel.selectedNotes else null
                    )
                )
                if (noteListScreenViewModel.showDeleteConfirmationDialog) {
                    DeleteConfirmationDialog(
                        numberOfNoteToBeDelete = noteListScreenViewModel.selectedNotes.size,
                        onDismissRequest = noteListScreenViewModel::onDeleteIconClick,
                        onConfirm = noteListScreenViewModel::deleteAllSelectedNote
                    )
                }
                if (noteListScreenViewModel.showSetPasswordDialogBox) {
                    SetPasswordDialog(
                        onDismissRequest = noteListScreenViewModel::onLockIconClick,
                        onSetPasswordClick = noteListScreenViewModel::setPasswordForAllSelectedNote
                    )
                }
            } else if (noteListScreenViewModel.searchBarTextValue.isNotEmpty()) {
                EmptySearchResult(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                )
            } else {
                EmptyNoteList(onAddNoteClicked = navigateToAddNoteScreen)
            }
        } else {
            /**
             * Device is still loading data from the room database.
             */
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}