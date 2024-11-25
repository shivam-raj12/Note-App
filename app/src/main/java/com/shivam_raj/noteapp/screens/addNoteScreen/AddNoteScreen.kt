package com.shivam_raj.noteapp.screens.addNoteScreen

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavHostController
import com.shivam_raj.noteapp.database.Note
import com.shivam_raj.noteapp.database.Priority
import com.shivam_raj.noteapp.navigation.Screens
import com.shivam_raj.noteapp.screens.addNoteScreen.viewModel.AddNoteScreenViewModel
import com.shivam_raj.noteapp.screens.noteListScreen.noteRepo
import com.shivam_raj.noteapp.screens.noteSecurityScreen.SecurityData

@Composable
fun AddNoteScreen(
    navigator: NavHostController,
    securityData: SecurityData,
    note: Note? = null
) {
    val addNoteScreenViewModel: AddNoteScreenViewModel = viewModel(factory = viewModelFactory {
        initializer {
            AddNoteScreenViewModel(noteRepo().noteRepository, note)
        }
    })
    addNoteScreenViewModel.securityData = securityData
    val focusManager = LocalFocusManager.current

    val topBarButtonEnabled by remember {
        derivedStateOf {
            (addNoteScreenViewModel.title.value.isNotEmpty() && addNoteScreenViewModel.description.value.isNotEmpty())
        }
    }
    Scaffold(
        modifier = Modifier
            .imePadding()
            .fillMaxSize(),
        topBar = {
            TopBar(
                priority = if (note != null) Priority.getValueWithId(note.notePriority) else null,
                enabled = topBarButtonEnabled,
                isNewNote = note == null,
                onSecurityClick = {
                    navigator.navigate(
                        Screens.SecurityScreen.route
                    )
                },
                onBackArrowClick = {
                    navigator.popBackStack()
                },
                colorIndex = note?.colorIndex,
                onSaveClick = { priority, colorIndex ->
                    addNoteScreenViewModel.onSaveNoteButtonClick(priority, colorIndex)
                    navigator.popBackStack()
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding()
                )
                .padding(
                    horizontal = 8.dp,
                    vertical = 5.dp
                )
                .verticalScroll(rememberScrollState())
                .animateContentSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedTextField(
                value = addNoteScreenViewModel.title.value,
                onValueChange = { if (it.length <= 60) addNoteScreenViewModel.setTitle(it) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(text = "Note's Title")
                },
                textStyle = MaterialTheme.typography.titleLarge,
                suffix = {
                    Text(text = "${addNoteScreenViewModel.title.value.length}/60")
                },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    autoCorrectEnabled = true,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                )
            )
            OutlinedTextField(
                value = addNoteScreenViewModel.description.value,
                onValueChange = addNoteScreenViewModel::setDescription,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                placeholder = {
                    Text(text = "Note's Description")
                },
                textStyle = MaterialTheme.typography.bodyLarge,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    autoCorrectEnabled = true,
                    keyboardType = KeyboardType.Text
                ),
            )
        }
    }
}