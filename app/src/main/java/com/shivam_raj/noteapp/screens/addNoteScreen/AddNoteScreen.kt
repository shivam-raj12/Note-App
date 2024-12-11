package com.shivam_raj.noteapp.screens.addNoteScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.shivam_raj.noteapp.database.Note
import com.shivam_raj.noteapp.navigation.Screens
import com.shivam_raj.noteapp.screens.addNoteScreen.viewModel.AddNoteScreenViewModel
import com.shivam_raj.noteapp.screens.noteSecurityScreen.SecurityData

@Composable
fun AddNoteScreen(
    navigator: NavHostController,
    addNoteScreenViewModel: AddNoteScreenViewModel = hiltViewModel(),
    securityData: SecurityData,
    note: Note? = null
) {
    LaunchedEffect(note) {
        addNoteScreenViewModel.updateNoteData(note?.noteTitle, note?.noteDescription)
        addNoteScreenViewModel.updateSecurityData(
            SecurityData(
                password = note?.password,
                fakeTitle = note?.fakeTitle,
                fakeDescription = note?.fakeDescription
            )
        )
    }
    LaunchedEffect(securityData) {
        addNoteScreenViewModel.updateSecurityData(securityData)
    }
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
                priority = note?.notePriority,
                enabled = topBarButtonEnabled,
                isNewNote = note == null,
                onSecurityClick = {
                    navigator.navigate(
                        Screens.SecurityScreen.route
                    )
                },
                onBackArrowClick = {
                    navigator.navigateUp()
                },
                colorIndex = note?.colorIndex,
                onSaveClick = { priority, colorIndex ->
                    addNoteScreenViewModel.onSaveNoteButtonClick(note,priority, colorIndex)
                    navigator.navigateUp()
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
                )
        ) {
            OutlinedTextField(
                value = addNoteScreenViewModel.title.value,
                onValueChange = { addNoteScreenViewModel.setTitle(it) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(text = "Note's Title")
                },
                textStyle = MaterialTheme.typography.titleLarge,
                singleLine = true,
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
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                )
            )
            HorizontalDivider()
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
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                )
            )
        }
    }
}