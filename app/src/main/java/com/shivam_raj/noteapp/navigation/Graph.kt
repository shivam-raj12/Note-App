package com.shivam_raj.noteapp.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.shivam_raj.noteapp.database.EmptyNote
import com.shivam_raj.noteapp.screens.SharedViewModel
import com.shivam_raj.noteapp.screens.addNoteScreen.AddNoteScreen
import com.shivam_raj.noteapp.screens.detailNoteScreen.DetailNoteScreen
import com.shivam_raj.noteapp.screens.noteListScreen.NoteHomeScreen
import com.shivam_raj.noteapp.screens.noteSecurityScreen.SecurityData
import com.shivam_raj.noteapp.screens.noteSecurityScreen.SetPasswordScreen
import com.shivam_raj.noteapp.screens.passwordScreen.PasswordScreen

@Composable
fun NavigationGraph() {
    val navController: NavHostController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screens.HomeScreen.route,
        route = "root"
    ) {
        animatedComposable(
            route = Screens.HomeScreen.route
        ) {
            val viewModel: SharedViewModel = it.sharedViewModel<SharedViewModel>(navController)
            NoteHomeScreen(
                navigator = navController,
                onNoteClick = { note ->
                    viewModel.selectedNote = note
                },
                clearNote = {
                    viewModel.selectedNote = null
                    viewModel.securityData = SecurityData()
                }
            )
        }
        animatedComposable(Screens.DetailScreen.route) {
            val viewModel: SharedViewModel = it.sharedViewModel<SharedViewModel>(navController)
            DetailNoteScreen(
                navigator = navController,
                note = viewModel.selectedNote ?: EmptyNote
            )
        }
        animatedComposable(Screens.AddNoteScreen.route) {
            val viewModel: SharedViewModel = it.sharedViewModel<SharedViewModel>(navController)
            AddNoteScreen(
                navigator = navController,
                securityData = viewModel.securityData,
                note = viewModel.selectedNote
            )
        }
        animatedComposable(Screens.PasswordScreen.route) {
            val viewModel: SharedViewModel = it.sharedViewModel<SharedViewModel>(navController)
            PasswordScreen(
                navigator = navController,
                notePassword = viewModel.selectedNote?.password
            )
        }
        animatedComposable(Screens.SecurityScreen.route) {
            val viewModel: SharedViewModel = it.sharedViewModel<SharedViewModel>(navController)
            SetPasswordScreen(
                navigator = navController,
                defaultSecurityData = SecurityData(
                    password = viewModel.selectedNote?.password,
                    fakeTitle = viewModel.selectedNote?.fakeTitle,
                    fakeDescription = viewModel.selectedNote?.fakeDescription
                ),
                onSecurityDataChange = { securityData ->
                    viewModel.securityData = securityData
                }
            )
        }
    }
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(navController: NavHostController): T {
    val navGraphRoute = destination.parent?.route ?: return viewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return viewModel(parentEntry)
}


fun NavGraphBuilder.animatedComposable(
    route: String,
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) {
    composable(
        route = route,
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(700)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(700)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(700)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(700)
            )
        },
        content = content
    )
}