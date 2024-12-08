package com.shivam_raj.noteapp.screens.addNoteScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.shivam_raj.noteapp.database.Priority
import com.shivam_raj.noteapp.screens.addNoteScreen.bottomSheet.BottomSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    priority: Priority? = null,
    colorIndex: Int?,
    enabled: Boolean,
    isNewNote:Boolean,
    onBackArrowClick: () -> Unit,
    onSecurityClick: () -> Unit,
    onSaveClick: (Priority, Int?) -> Unit
) {
    var show by remember {
        mutableStateOf(false)
    }
    TopAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding(),
        title = {
            Text(text = "Add your note")
        },
        navigationIcon = {
            IconButton(onClick = onBackArrowClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        actions = {
            IconButton(onClick = onSecurityClick) {
                Icon(
                    imageVector = Icons.Outlined.Lock,
                    contentDescription = "Security"
                )
            }
            FilledTonalButton(
                onClick = { show = true },
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp),
                enabled = enabled,
                shape = RoundedCornerShape(15.dp)
            ) {
                Text(text = "Next")
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null
                )
            }
        }
    )
    if (show) {
        BottomSheet(
            priority = priority,
            isNewNote = isNewNote,
            onSaveClick = onSaveClick,
            onDismissRequest = { show = false },
            noteBackgroundColorIndex = colorIndex,
            onSecurityClick = onSecurityClick
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextWithIcon(
    modifier: Modifier,
    text: String,
    expanded: Boolean
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = text)
        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
    }
}