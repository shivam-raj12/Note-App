package com.shivam_raj.noteapp.screens.noteListScreen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavHostController
import com.shivam_raj.noteapp.Application
import com.shivam_raj.noteapp.database.Note
import com.shivam_raj.noteapp.navigation.Screens
import com.shivam_raj.noteapp.screens.noteListScreen.utils.ActionModeIconsList
import com.shivam_raj.noteapp.screens.noteListScreen.utils.DeleteConfirmationDialog
import com.shivam_raj.noteapp.screens.noteListScreen.utils.EmptyNoteList
import com.shivam_raj.noteapp.screens.noteListScreen.utils.EmptySearchResult
import com.shivam_raj.noteapp.screens.noteListScreen.utils.NoteHomeScreenFloatingButton
import com.shivam_raj.noteapp.screens.noteListScreen.utils.SetPasswordDialog
import com.shivam_raj.noteapp.screens.noteListScreen.viewModel.NoteListScreenViewModel

/**
 * A composable destination function and also a starting destination for the app.
 *
 * This app calls various other composable function depends on if the user saved any note or not. Below are the conditions:
 *
 *  - If the note list returns null, it means the device is still loading the saved data from the room database. In this case a CircularProgressBar is shown.
 *  - If the note list is an empty list, it means the user haven't saved any notes yet. In this case [EmptyNoteList] composable is shown.
 *  - If the note list is not empty, it means the user have some saved notes. In this case all the notes with [NoteList] composable is shown.
 *
 * The top bar of this composable [NoteListTopBar] contains a search field. This search field also creates two possibilities:
 *  - If the search field is empty, it means the user haven't searched for any note. In this case we are showing all the saved notes of the user.
 *  - If the search field is not empty, it means the user have searched for any specific note. In this case we filer our list in following ways:
 *      - We filter our noteList with those notes whose title or description starts with the value of search field.
 *      - If the note contains fake title, then we ignores the original note's title and filter noteList with those notes whose fake title matches search field value.
 *      - If the note contains fake description, then we ignores the original note's description and filter noteList with those notes whose fake description matches search field value.
 *          - Reason for searching in fake title or fake description instead of original title and description is that if note contains fake title and fake description, we don't show the original title and description on the homeScreen. We only show original title and description only on DetailNoteScreen.
 *  - If the list of filtered note is empty then we show [EmptySearchResult] composable function.
 *
 *  This composable function also shows two dialogBox: [DeleteConfirmationDialog] and [SetPasswordDialog] . These dialog box are shown when the delete icon or set password icon of [NoteListTopBar] are clicked in actionMode respectively.
 * @param noteListScreenViewModel ViewModel for this composable function.
 * @see NoteListScreenViewModel
 */
@Composable
fun NoteHomeScreen(
    noteListScreenViewModel: NoteListScreenViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                NoteListScreenViewModel(noteRepo().noteRepository)
            }
        }
    ),
    navigator: NavHostController,
    onNoteClick:(Note)->Unit
) {
    BackHandler(
        enabled = noteListScreenViewModel.isSelectionModeActive
    ) {
        noteListScreenViewModel.reverseSelectionMode()
    }

    val navigateToAddNoteScreen: () -> Unit = { navigator.navigate(Screens.AddNoteScreen.route) }

    /**
     * Fetching all the saved notes and applying search field filter.
     *  - It will return null, if the data from room database are still loading
     *  - It will return empty list, if there is no any saved note
     */
    val noteList: List<Note>? =
        noteListScreenViewModel.noteList.collectAsState(initial = null).value?.filter {
            if (noteListScreenViewModel.searchBarTextValue.isEmpty()) {
                true
            } else if (noteListScreenViewModel.searchBarTextValue.isNotEmpty()) {
                (((it.fakeTitle ?: it.noteTitle)
                    .startsWith(
                        prefix = noteListScreenViewModel.searchBarTextValue,
                        ignoreCase = true
                    )) ||
                        ((it.fakeDescription ?: it.noteDescription).startsWith(
                            prefix = noteListScreenViewModel.searchBarTextValue,
                            ignoreCase = true
                        )))
            } else {
                false
            }
        }
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
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
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

fun CreationExtras.noteRepo(): Application {
    return (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]) as Application
}