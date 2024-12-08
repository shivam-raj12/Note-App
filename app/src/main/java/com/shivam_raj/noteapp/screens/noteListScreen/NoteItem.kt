package com.shivam_raj.noteapp.screens.noteListScreen

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.shivam_raj.noteapp.R
import com.shivam_raj.noteapp.database.Note
import com.shivam_raj.noteapp.getStandardFormattedDate
import com.shivam_raj.noteapp.screens.noteListScreen.utils.DeleteConfirmationDialog
import com.shivam_raj.noteapp.ui.theme.colorProviderForNoteBackground

private const val CUT_RADIUS = 20

/**
 * This composable function represents a single item of a [NoteList].
 *  - This function also shows a confirmation dialog box when the user click on delete note button, if the users confirms it then [onDeleteNoteIconClick] callback is called.
 * @param modifier Modifier to be applied to this layout
 * @param note An instance of [Note] class which is to represent by this item
 * @param onDeleteNoteIconClick This callback will be called when the item's delete icon button is clicked.
 * @param onNoteLongClick This callback will be called when the user long presses the note
 * @param onNoteClick This callback is called when there is click event on this note
 * @param isSelected A boolean or null value to represent whether the note is selected or not.
 * @see DeleteButton
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteItem(
    modifier: Modifier = Modifier,
    note: Note,
    onDeleteNoteIconClick: (Note) -> Unit,
    onNoteLongClick: (Note) -> Unit,
    onNoteClick: (Note) -> Unit,
    isSelected: Boolean?
) {

    var showDialog by remember {
        mutableStateOf(false)
    }
    val isSystemInDarkTheme = isSystemInDarkTheme()

    val colorFamily = remember {
        colorProviderForNoteBackground(
            isDarkTheme = isSystemInDarkTheme,
            notePriority = note.notePriority,
            noteCustomColorIndex = note.colorIndex
        )
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(
                shape = CutCornerShape(topStart = CUT_RADIUS.dp, bottomEnd = CUT_RADIUS.dp)
            )
            .background(
                colorFamily.colorContainer
            )
            .combinedClickable(
                onClick = {
                    onNoteClick(note)
                },
                onLongClick = {
                    onNoteLongClick(note)
                }
            ),
        horizontalAlignment = Alignment.End
    ) {
        CompositionLocalProvider(
            LocalContentColor provides colorFamily.onColorContainer
        ) {
            Row {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp, 16.dp)
                ) {
                    Row {
                        if (note.password != null) {
                            Icon(
                                imageVector = Icons.Outlined.Lock,
                                contentDescription = null
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                        Text(
                            text = note.fakeTitle ?: note.noteTitle,
                            style = MaterialTheme.typography.headlineSmall,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = note.fakeDescription ?: note.noteDescription,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                DeleteButton(isSelected = isSelected) {
                    showDialog = true
                }
            }
            Row(
                modifier = Modifier.padding(end = CUT_RADIUS.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (note.pinnedAt != null) {
                    Icon(
                        painter = painterResource(id = R.drawable.pin),
                        contentDescription = null,
                        modifier = Modifier
                            .size(18.dp)
                            .alpha(0.6f)
                    )
                }
                Text(
                    text = getStandardFormattedDate(millisecond = note.lastUpdate),
                    modifier = Modifier
                        .alpha(0.6f),
                    style = MaterialTheme.typography.labelSmall,
                )
            }
        }
    }
    if (showDialog) {
        DeleteConfirmationDialog(
            notePassword = note.password,
            onDismissRequest = {
                showDialog = false
            },
            onConfirm = { onDeleteNoteIconClick(note) }
        )
    }
}

/**
 * Composable function which represents the delete button for [NoteItem]
 *  - This delete button is based on the value passed to the [isSelected] parameter
 *      - If it is null, then normal delete icon button will be shown
 *      - If it is false, it means all the notes are in action mode but this note is not being selected. In this case, it will show a bordered circle.
 *          - Now clicking on this note will select this note too.
 *      - If it is true , it means all the notes are in action mode and this note is also being selected. In this case, it will show a filled circle with a tick icon inside it,
 *          - Now clicking on this note will deselect this note.
 * @param isSelected A boolean or null value to represent whether the note is selected or not.
 * @param onClick This callback will be called when the delete icon is clicked. It is only possible when isSelected value is null.
 */
@Composable
fun DeleteButton(
    isSelected: Boolean?,
    onClick: () -> Unit
) {
    AnimatedContent(targetState = isSelected == null, label = "Delete button animation") {
        if (it) {
            IconButton(onClick = onClick) {
                Icon(imageVector = Icons.Filled.Delete, contentDescription = "Delete note")
            }
        } else if (isSelected != null) {
            Box(
                modifier = Modifier
                    .padding(top = 12.dp, end = 12.dp)
                    .size(24.dp)
                    .then(
                        if (!isSelected) {
                            Modifier.border(2.dp, MaterialTheme.colorScheme.scrim, CircleShape)
                        } else {
                            Modifier.background(
                                MaterialTheme.colorScheme.inverseSurface, CircleShape
                            )
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Filled.Done,
                        tint = MaterialTheme.colorScheme.inverseOnSurface,
                        contentDescription = "Selected note"
                    )
                }
            }
        }
    }
}
