package com.shivam_raj.noteapp.screens.noteSecurityScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

@Composable
fun HintDialogBox(
    expandedHintDialog:Boolean,
    onDismissHintDialog:()->Unit,
    hintText:String
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopStart
    ) {
        DropdownMenu(
            offset = DpOffset(x = 0.dp, y = 12.dp),
            expanded = expandedHintDialog,
            onDismissRequest = onDismissHintDialog,
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Text(
                text = hintText,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier
                    .alpha(0.8f)
                    .padding(12.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}