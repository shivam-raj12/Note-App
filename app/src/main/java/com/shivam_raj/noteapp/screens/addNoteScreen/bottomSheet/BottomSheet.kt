package com.shivam_raj.noteapp.screens.addNoteScreen.bottomSheet

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.shivam_raj.noteapp.database.Priority
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    priority: Priority?,
    isNewNote:Boolean,
    noteBackgroundColorIndex: Int? = null,
    onSaveClick: (Priority, Int?) -> Unit,
    onSecurityClick:()->Unit,
    onDismissRequest: () -> Unit,
) {
    var selectedPriority by remember {
        mutableStateOf(priority ?: Priority.entries.toTypedArray().random())
    }

    var backgroundColor: Int? by remember {
        mutableStateOf(noteBackgroundColorIndex)
    }

    val sheetState = rememberModalBottomSheetState(true)
    val coroutineScope = rememberCoroutineScope()
    ModalBottomSheet(
        modifier = Modifier
            .imePadding(),
        sheetState = sheetState,
        onDismissRequest = {
            coroutineScope.launch {
                sheetState.hide()
                onDismissRequest()
            }
        },
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 6.dp)
                .animateContentSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(
                    onClick = {
                        coroutineScope.launch {
                            sheetState.hide()
                            onDismissRequest()
                        }
                    }
                ) {
                    Icon(imageVector = Icons.Filled.Clear, contentDescription = "Close sheet")
                }
            }
            BottomSheetSecurityItem{
                coroutineScope.launch {
                    sheetState.hide()
                    onDismissRequest()
                    onSecurityClick()
                }
            }
            SetPriorityBottomSheetItem(
                selectedPriority = selectedPriority,
                onPriorityChanged = {
                    selectedPriority = it
                }
            )
            NoteBackgroundColorSheetItem(
                noteBackgroundColorIndex = backgroundColor ?: -1,
                onCheckedChange = {
                    backgroundColor = it
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            FilledTonalButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    coroutineScope.launch {
                        sheetState.hide()
                        onDismissRequest()
                    }
                    onSaveClick(
                        selectedPriority,
                        backgroundColor
                    )
                }
            ) {
                Text(text = if (isNewNote) "Save note" else "Update note")
            }
        }
    }
}