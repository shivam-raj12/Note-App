package com.shivam_raj.noteapp.screens.noteSecurityScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
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
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * A composable function which only represents the fake note cover field in [SetPasswordScreen]
 * @param fakeTitle  the input fake title to be shown in the text field
 * @param fakeDescription  the input fake description to be shown in the text field
 * @param onFakeTitleValueChange  the callback is triggered when the input service updates the fakeTitle. An updated fakeTitle comes as a parameter of the callback.
 * @param onFakeDescriptionValueChange  the callback is triggered when the input service updates the fakeDescription. An updated fakeDescription comes as a parameter of the callback.
 */
@Composable
fun FakeNoteCover(
    fakeTitle: String,
    fakeDescription: String,
    onFakeTitleValueChange: (String) -> Unit,
    onFakeDescriptionValueChange: (String) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    var showHintDialog by remember {
        mutableStateOf(false)
    }
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Fake Note Cover",
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                fontSize = 19.sp
            )
            IconButton(onClick = { focusManager.clearFocus(); showHintDialog = !showHintDialog }) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = "Show use",
                    modifier = Modifier.fillMaxWidth()
                )
            }
            HintDialogBox(
                expandedHintDialog = showHintDialog,
                onDismissHintDialog = { showHintDialog = false },
                hintText = "Keep your note's title and description hidden on the home screen.\nFor added security, we recommend setting a password for your notes when using this fake note cover feature.This ensures that your notes remain fully secure."
            )
        }
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = fakeTitle,
            onValueChange = onFakeTitleValueChange,
            placeholder = {
                Text(text = "Set your fake title")
            },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                autoCorrectEnabled = true,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = fakeDescription,
            onValueChange = onFakeDescriptionValueChange,
            placeholder = {
                Text(text = "Set your fake description")
            },
            minLines = 4,
            maxLines = 5,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                autoCorrectEnabled = true,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }
            )
        )
    }
}
