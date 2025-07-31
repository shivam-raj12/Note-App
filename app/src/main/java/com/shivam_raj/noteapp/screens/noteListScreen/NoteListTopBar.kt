package com.shivam_raj.noteapp.screens.noteListScreen

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shivam_raj.noteapp.R

/**
 * A composable function which displays the topBar of a [NoteHomeScreen].
 *
 * It also contains a searchBar field in both the states which is used to search notes.
 *
 * This topBar displays two state:
 * - When selection mode is active. If this mode is active and a note is being clicked, then it will select the clicked note instead of opening DetailNoteScreen.
 * - When selection mode is inactive. It is normal state, if the note is clicked in this mode, it will simply show a DetailNoteScreen (or a PasswordScreen if the note is password protected).
 *
 * @param modifier Modifier to be applied to this layout
 * @param actionModeText Text to be shown when actionMode is active. It is used to display the number of note being selected.
 * @param value A string value being used in search bar text field
 * @param onValueChange A callback is called when the search bar text field value changes.
 * @param onClearFilterClicked This callback is called when the trailing Icon of search bar text field will be clicked. It is simply called to clear the filter i.e. make the [value] empty
 * @param onCloseActionModeClick This callback is called when the close button of actionMode is clicked. This will close the actionMode.
 * @param showAction Whether to display action mode or normal mode.
 * @param content content of the actionMode except text and close icon.
 * @see [com.shivam_raj.noteapp.screens.noteListScreen.utils.ActionModeIconsList]
 */
@Composable
fun NoteListTopBar(
    modifier: Modifier = Modifier,
    actionModeText: String,
    value: String,
    onValueChange: (String) -> Unit,
    onClearFilterClicked: () -> Unit,
    onCloseActionModeClick: () -> Unit,
    showAction: Boolean = false,
    content: @Composable RowScope.() -> Unit
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier,
    ) {
        AnimatedContent(targetState = showAction, label = "") {
            if (it) {
                ActionMode(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .height(48.dp),
                    onClick = onCloseActionModeClick,
                    actionModeText = actionModeText,
                    content = content,
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .height(48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Your Notes",
                        style = MaterialTheme.typography.headlineSmall,
                        fontFamily = FontFamily(
                            Font(
                                R.font.inknut_antiqua_medium,
                                FontWeight.Medium
                            )
                        ),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
            shape = RoundedCornerShape(15.dp),
            placeholder = {
                Text(text = "Search your notes")
            },
            leadingIcon = {
                Icon(imageVector = Icons.Outlined.Search, contentDescription = null)
            },
            trailingIcon = {
                if (value.isNotEmpty()) {
                    IconButton(onClick = {
                        onClearFilterClicked()
                        focusManager.clearFocus()
                    }) {
                        Icon(imageVector = Icons.Filled.Clear, contentDescription = "Clear filter")
                    }
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                focusedLeadingIconColor = MaterialTheme.colorScheme.onSurface,
                unfocusedLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                focusedTrailingIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                unfocusedTrailingIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface,
                unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    focusManager.clearFocus()
                }
            )
        )
    }
}

@Composable
fun ActionMode(
    modifier: Modifier,
    onClick: () -> Unit,
    actionModeText: String,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onClick) {
            Icon(imageVector = Icons.Filled.Clear, contentDescription = null)
        }
        Text(
            text = actionModeText,
            style = MaterialTheme.typography.headlineMedium,
        )
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
            content = content
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TopBarPreview() {
    NoteListTopBar(
        actionModeText = "1",
        value = "",
        onValueChange = {},
        onClearFilterClicked = {},
        onCloseActionModeClick = {}
    ) { }
}