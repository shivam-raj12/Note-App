package com.shivam_raj.noteapp.screens.noteListScreen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.shivam_raj.noteapp.R


enum class TopBarState {
    NORMAL,
    SEARCH,
    ACTION_MODE
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteListTopBar(
    actionModeText: String,
    value: String,
    onValueChange: (String) -> Unit,
    onClearFilterClicked: () -> Unit,
    onCloseActionModeClick: () -> Unit,
    showAction: Boolean = false,
    content: @Composable RowScope.() -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    var topBarState by remember(showAction) {
        mutableStateOf(if (showAction) TopBarState.ACTION_MODE else TopBarState.NORMAL)
    }

    BackHandler(
        enabled = topBarState == TopBarState.SEARCH || topBarState == TopBarState.ACTION_MODE
    ) {
        onClearFilterClicked()
        if (topBarState == TopBarState.ACTION_MODE) onCloseActionModeClick()
        topBarState = TopBarState.NORMAL
    }
    LaunchedEffect(topBarState) {
        if (topBarState == TopBarState.SEARCH) {
            focusRequester.requestFocus()
        }
    }
    AnimatedContent(
        targetState = topBarState
    ) {
        when (it) {
            TopBarState.NORMAL -> {
                TopAppBar(
                    title = {
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
                    },
                    actions = {
                        IconButton(onClick = {
                            topBarState = if (showAction) TopBarState.ACTION_MODE else TopBarState.SEARCH
                        }) {
                            Icon(imageVector = Icons.Outlined.Search, contentDescription = null)
                        }
                    }
                )
            }

            TopBarState.SEARCH -> {
                Column {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .statusBarsPadding()
                            .padding(start = 12.dp, end = 12.dp)
                            .focusRequester(focusRequester),
                        value = value,
                        onValueChange = onValueChange,
                        shape = RoundedCornerShape(15.dp),
                        placeholder = {
                            Text(text = "Search your notes")
                        },
                        leadingIcon = {
                            IconButton(
                                onClick = {
                                    topBarState = if (showAction) TopBarState.ACTION_MODE else TopBarState.NORMAL
                                    onClearFilterClicked()
                                }
                            ) {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowBack,
                                    null
                                )
                            }
                        },
                        trailingIcon = {
                            if (value.isNotEmpty()) {
                                IconButton(onClick = {
                                    topBarState = if (showAction) TopBarState.ACTION_MODE else TopBarState.NORMAL
                                    onClearFilterClicked()
                                }) {
                                    Icon(
                                        imageVector = Icons.Filled.Clear,
                                        contentDescription = "Clear filter"
                                    )
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
                    Spacer(Modifier.height(5.dp))
                }
            }

            TopBarState.ACTION_MODE -> {
                ActionMode(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .height(48.dp),
                    onClick = onCloseActionModeClick,
                    actionModeText = actionModeText,
                    content = content,
                )
            }
        }
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
