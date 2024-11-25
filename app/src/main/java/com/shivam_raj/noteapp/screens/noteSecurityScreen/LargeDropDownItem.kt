package com.shivam_raj.noteapp.screens.noteSecurityScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.shivam_raj.noteapp.R

/**
 * Composable function for dropdown menu. It will display all those notes which are already password protected.
 * @param modifier Modifier to be applied to this layout
 * @param title Title of the note
 * @param description Description of the note
 * @param password Password of the note.
 * @param onClick This callback will be called when the item will be clicked.
 */
@Composable
fun LargeDropDownMenuItem(
    modifier: Modifier,
    title: String,
    description: String,
    password: String,
    onClick: () -> Unit
) {
    var passwordVisibility by remember {
        mutableStateOf(false)
    }
    Row(
        modifier = modifier
            .heightIn(48.dp)
            .clickable(
                onClick = onClick,
                role = Role.DropdownList
            )
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurface) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurfaceVariant) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = if (passwordVisibility) password else "*".repeat(password.length))
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                    Icon(painter = painterResource(id = if (passwordVisibility) R.drawable.password_visibility_off else R.drawable.password_visible), contentDescription = null)
                }
            }
        }
    }
}
