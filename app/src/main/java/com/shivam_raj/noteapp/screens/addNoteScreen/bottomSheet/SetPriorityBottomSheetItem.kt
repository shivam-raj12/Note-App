package com.shivam_raj.noteapp.screens.addNoteScreen.bottomSheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import com.shivam_raj.noteapp.database.Priority
import com.shivam_raj.noteapp.screens.addNoteScreen.TextWithIcon


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetPriorityBottomSheetItem(
    selectedPriority: Priority,
    onPriorityChanged: (Priority) -> Unit
) {
    var expanded by remember {
        mutableStateOf(false)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 10.dp)
                .weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Priority", style = MaterialTheme.typography.titleMedium)
            Text(
                text = "Set how important your note is",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.alpha(0.6f)
            )
        }
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }) {
            TextWithIcon(
                text = selectedPriority.name,
                modifier = Modifier
                    .menuAnchor(
                        type = MenuAnchorType.PrimaryNotEditable,
                        enabled = true
                    )
                    .width(100.dp),
                expanded = expanded
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }) {
                Priority.entries.forEach { priority ->
                    DropdownMenuItem(
                        text = { Text(text = priority.name) },
                        onClick = { onPriorityChanged(priority);expanded = false })
                }
            }
        }
    }
}