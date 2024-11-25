package com.shivam_raj.noteapp.screens.noteSecurityScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

/**
 * A composable destination function for entering password, fake Title, fake Description for the note.
 * @param defaultSecurityData An instance of [SecurityData] class, if the note already contains a default value of password, fake title and fake description. This happens only when the user is editing existing note.
 * @return An instance of [SecurityData] class to the AddNoteScreen destination which contains password, fake Title and fake Description.
 * */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetPasswordScreen(
    defaultSecurityData: SecurityData = SecurityData(),
    navigator: NavHostController,
    onSecurityDataChange: (SecurityData) -> Unit
) {
    var fakeTitle by remember {
        mutableStateOf(defaultSecurityData.fakeTitle ?: "")
    }

    var fakeDescription by remember {
        mutableStateOf(defaultSecurityData.fakeDescription ?: "")
    }

    var password by remember {
        mutableStateOf(defaultSecurityData.password ?: "")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .systemBarsPadding()
            .verticalScroll(rememberScrollState())
    ) {
        CompositionLocalProvider(value = LocalContentColor provides MaterialTheme.colorScheme.onBackground) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CenterAlignedTopAppBar(
                    navigationIcon = {
                        IconButton(onClick = { navigator.navigateUp() }) {
                            Icon(imageVector = Icons.Rounded.Close, contentDescription = "Close")
                        }
                    },
                    title = {
                        Text(
                            text = "Security",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif,
                            modifier = Modifier
                                .alpha(0.8f),
                            textAlign = TextAlign.Center
                        )
                    }
                )
                HorizontalDivider()
                Spacer(modifier = Modifier.height(16.dp))
                SetPassword(
                    password = password,
                    onPasswordValueChange = { password = it }
                )
                Spacer(modifier = Modifier.height(16.dp))
                FakeNoteCover(
                    fakeTitle = fakeTitle,
                    fakeDescription = fakeDescription,
                    onFakeTitleValueChange = { fakeTitle = it },
                    onFakeDescriptionValueChange = { fakeDescription = it }
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = {
                    onSecurityDataChange(defaultSecurityData.copy(
                        password = password.ifBlank { null },
                        fakeTitle = fakeTitle.ifBlank { null },
                        fakeDescription = fakeDescription.ifBlank { null }
                    ))
                    navigator.navigateUp()
                }) {
                    Text(text = "Save Changes")
                }
            }
        }
    }
}