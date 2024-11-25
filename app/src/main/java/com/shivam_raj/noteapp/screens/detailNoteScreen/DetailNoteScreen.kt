package com.shivam_raj.noteapp.screens.detailNoteScreen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.shivam_raj.noteapp.R
import com.shivam_raj.noteapp.database.Note
import com.shivam_raj.noteapp.database.Priority
import com.shivam_raj.noteapp.getSimpleFormattedDate
import com.shivam_raj.noteapp.navigation.Screens
import com.shivam_raj.noteapp.ui.theme.colorProviderForNoteBackground

/**
 * This screen will show the selected note in detail.
 *  - Note title code will be found in topBar section. It is being shown with LargeTopAppBar.
 *  - Note description and time is in content section of Scaffold
 * @param note Instance of [Note] class which will be shown in detail.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailNoteScreen(
    navigator: NavHostController,
    note: Note
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val colorFamily = colorProviderForNoteBackground(
        isDarkTheme = isSystemInDarkTheme(),
        notePriority = Priority.getValueWithId(note.notePriority),
        noteCustomColorIndex = note.colorIndex
    )
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    Scaffold(
        modifier = Modifier
            .imePadding()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = colorFamily.colorContainer,
        contentColor = colorFamily.onColorContainer,
        topBar = {
            LargeTopAppBar(
                scrollBehavior = scrollBehavior,
                title = {
                    Text(
                        text = note.noteTitle,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navigator.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Go back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        clipboardManager.setText(
                            buildAnnotatedString {
                                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("Title: ")
                                }
                                append(note.noteTitle)
                                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("\nDescription: ")
                                }
                                append(note.noteDescription)
                                withStyle(SpanStyle(fontWeight = FontWeight.Light)) {
                                    append("\nCreated at ${getSimpleFormattedDate(note.dateAdded)}")
                                }
                            }
                        )
                        Toast.makeText(context, "Note copied", Toast.LENGTH_SHORT).show()
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.copy),
                            contentDescription = "Copy content"
                        )
                    }
                    IconButton(onClick = {
                        navigator.navigate(Screens.AddNoteScreen.route) {
                            popUpTo(Screens.HomeScreen.route)
                        }
                    }) {
                        Icon(imageVector = Icons.Filled.Edit, contentDescription = "Edit note")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = Color.Transparent,
                    navigationIconContentColor = colorFamily.onColorContainer,
                    titleContentColor = colorFamily.onColorContainer,
                    actionIconContentColor = colorFamily.onColorContainer
                ),
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(horizontal = 12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(0.8f)
                    .background(
                        colorFamily.color,
                        RoundedCornerShape(5.dp)
                    )
                    .padding(16.dp, 8.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(text = "Last update: ${getSimpleFormattedDate(note.lastUpdate)}", color = colorFamily.onColor)
                Text(text = "Created at: ${getSimpleFormattedDate(note.dateAdded)}", color = colorFamily.onColor)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = note.noteDescription,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(
                        rememberScrollState()
                    )
            )
        }
    }
}