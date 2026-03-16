package com.rishi.operater.navigation

sealed class AppDestination(val route: String, val label: String) {
    data object Home : AppDestination("home", "Home")
    data object Session : AppDestination("session", "Session")
    data object Settings : AppDestination("settings", "Settings")
}

val topLevelDestinations = listOf(
    AppDestination.Home,
    AppDestination.Session,
    AppDestination.Settings,
)
