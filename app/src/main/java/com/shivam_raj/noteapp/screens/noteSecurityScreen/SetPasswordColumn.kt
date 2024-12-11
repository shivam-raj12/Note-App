package com.shivam_raj.noteapp.screens.noteSecurityScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shivam_raj.noteapp.R
import com.shivam_raj.noteapp.database.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * A composable function which only represents the password field in [SetPasswordScreen].
 * @param password the input password to be shown in the text field
 * @param onPasswordValueChange the callback is triggered when the input service updates the password. An updated password comes as a parameter of the callback
 */
@Composable
fun SetPassword(
    password: String,
    onPasswordValueChange: (String) -> Unit
) {
    var showPassword by remember {
        mutableStateOf(false)
    }
    val focusManager = LocalFocusManager.current
    var expanded by remember { // Whether the drop down menu is expanded or not.
        mutableStateOf(false)
    }

    var showHintDialog by remember {
        mutableStateOf(false)
    }

    /**
     * List of all the saved notes which have already an existing password.
     *  - It is used to display all the previous password which user have been used for their notes. So that user can select note password among one of them.
     *  @see SetPasswordColumnViewModel
     */
    val list by hiltViewModel<SetPasswordColumnViewModel>().list.collectAsStateWithLifecycle(emptyList())
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Password",
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                fontSize = 19.sp
            )
            IconButton(onClick = {focusManager.clearFocus(); showHintDialog = !showHintDialog }) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = "Show use",
                    modifier = Modifier.fillMaxWidth()
                )
            }
            HintDialogBox(
                expandedHintDialog = showHintDialog,
                onDismissHintDialog = { showHintDialog = false },
                hintText = "Set a password to secure your notes. This ensures that only you can access them.\nFor easy management, consider using the same password for all notes.\nClick the side icon to select a password from your other notes."
            )
        }
        Row(
            verticalAlignment = Alignment.Top
        ) {
            OutlinedTextField(
                modifier = Modifier.weight(1f),
                value = password,
                onValueChange = onPasswordValueChange,
                placeholder = {
                    Text(text = "Set your note password")
                },
                singleLine = true,
                trailingIcon = {
                    if (password.isNotEmpty()) {
                        IconButton(onClick = { showPassword = !showPassword }) {
                            Icon(
                                painter = painterResource(id = if (showPassword) R.drawable.password_visibility_off else R.drawable.password_visible),
                                contentDescription = if (showPassword) "Hide password" else "Show password"
                            )
                        }
                    }
                },
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(
                    '*'
                ),
                keyboardOptions = KeyboardOptions(
                    autoCorrectEnabled = false,
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus()
                }),
                supportingText = {
                    Text(text = "It is recommended to keep all your note's password same.")
                }
            )
            Box {
                FilledIconButton(
                    onClick = {
                        focusManager.clearFocus()
                        expanded = !expanded
                    },
                ) {
                    Icon(
                        modifier = Modifier.rotate(90f),
                        painter = painterResource(id = R.drawable.filter),
                        contentDescription = "Select password from"
                    )
                }
                DropdownMenu(
                    expanded = expanded, onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .heightIn(max = 500.dp)
                ) {
                    if (list.isEmpty()) {
                        Text(
                            text = "No any other locked note found",
                            style = MaterialTheme.typography.titleMedium,
                            fontSize = 16.sp,
                            modifier = Modifier
                                .alpha(0.8f)
                                .padding(start = 16.dp)
                        )
                    } else {
                        Text(
                            text = "Select password from",
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier
                                .alpha(0.6f)
                                .padding(start = 16.dp)
                        )
                        list.forEach {
                            LargeDropDownMenuItem(
                                modifier = Modifier.fillMaxWidth(),
                                title = it.noteTitle,
                                description = it.noteDescription,
                                password = it.password ?: "",
                                onClick = {
                                    expanded = false
                                    onPasswordValueChange(it.password ?: "")
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Simple viewModel class for the [SetPassword] column,
 * @property list Represents the list of all the password protected notes.
 */

@HiltViewModel
class SetPasswordColumnViewModel @Inject constructor(noteRepository: NoteRepository) : ViewModel() {
    val list = noteRepository.getAllPasswordProtectedNote()
}