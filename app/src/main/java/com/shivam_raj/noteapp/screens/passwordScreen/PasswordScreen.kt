package com.shivam_raj.noteapp.screens.passwordScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.twotone.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.shivam_raj.noteapp.R
import com.shivam_raj.noteapp.ShakeConfig
import com.shivam_raj.noteapp.navigation.Screens
import com.shivam_raj.noteapp.rememberShakeController
import com.shivam_raj.noteapp.shake

/**
 * A composable destination function which is used to enter password of the note. This destination is being shown when a locked note is clicked.
 *  - If the user enters a correct password then DetailNoteScreen will be shown.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordScreen(
    notePassword: String?,
    navigator: NavHostController
) {
    if (notePassword.isNullOrBlank()){
        navigator.navigate(Screens.DetailScreen.route){
            popUpTo(Screens.PasswordScreen.route)
        }
    }

    var password by remember {
        mutableStateOf("")
    }
    var passwordVisibility by remember {
        mutableStateOf(false)
    }

    val shakeController = rememberShakeController()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .systemBarsPadding()
            .verticalScroll(rememberScrollState())
            .imePadding()
    ) {
        CompositionLocalProvider(value = LocalContentColor provides MaterialTheme.colorScheme.onBackground) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "Locked Note",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif,
                            modifier = Modifier.alpha(0.8f)
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navigator.popBackStack() }) {
                            Icon(imageVector = Icons.Rounded.Close, contentDescription = "Close")
                        }
                    }
                )
                HorizontalDivider()
                Image(
                    painter = painterResource(id = R.drawable.locked_note),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    modifier = Modifier.alpha(0.7f),
                    text = "Enter your note password to continue",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .shake(shakeController),
                    value = password,
                    onValueChange = { password = it },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        if (notePassword == password) {
                            navigator.navigate(Screens.DetailScreen.route) {
                                popUpTo(Screens.HomeScreen.route)
                            }
                        } else {
                            shakeController.shake()
                            password = ""
                        }
                    }),
                    visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(
                        '*'
                    ),
                    maxLines = 1,
                    singleLine = true,
                    placeholder = {
                        Text(text = "Note password")
                    },
                    leadingIcon = {
                        Icon(imageVector = Icons.TwoTone.Lock, contentDescription = null)
                    },
                    trailingIcon = {
                        if (password.isNotEmpty()) {
                            IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                                Icon(
                                    painter = painterResource(id = if (passwordVisibility) R.drawable.password_visibility_off else R.drawable.password_visible),
                                    contentDescription = null
                                )
                            }
                        }
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = {
                    if (notePassword == password) {
                        navigator.navigate(Screens.DetailScreen.route) {
                            popUpTo(Screens.HomeScreen.route)
                        }
                    } else {
                        shakeController.shake(ShakeConfig(2, 100f))
                        password = ""
                    }
                }, modifier = Modifier.fillMaxWidth(0.6f)) {
                    Text(text = "Open Note")
                }
            }
        }
    }
}


