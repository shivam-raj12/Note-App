package com.shivam_raj.noteapp.navigation

sealed class Screens(val route: String) {
    data object HomeScreen : Screens("home_screen")
    data object DetailScreen : Screens("detail_screen")
    data object AddNoteScreen : Screens("add_note_screen")
    data object SecurityScreen : Screens("security_screen")
    data object PasswordScreen : Screens("password_screen")
}