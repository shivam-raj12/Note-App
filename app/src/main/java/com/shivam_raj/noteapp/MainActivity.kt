package com.shivam_raj.noteapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.shivam_raj.noteapp.navigation.NavigationGraph
import com.shivam_raj.noteapp.ui.theme.NoteAppTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * MainActivity - Entry point of the app
 *  - enableEdgeToEdge() is used to make the status bar transparent.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            NoteAppTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                ) {
                    NavigationGraph()
                }
            }
        }
    }
}