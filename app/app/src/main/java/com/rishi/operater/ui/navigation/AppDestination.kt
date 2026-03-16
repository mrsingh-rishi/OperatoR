package com.rishi.operater.ui.navigation

sealed class AppDestination(val route: String) {
    data object Home : AppDestination("home")
    data object Session : AppDestination("session")
    data object Settings : AppDestination("settings")
}
