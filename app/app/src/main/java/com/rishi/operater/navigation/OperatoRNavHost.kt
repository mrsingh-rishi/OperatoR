package com.rishi.operater.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.rishi.operater.ui.screens.home.HomeScreen
import com.rishi.operater.ui.screens.home.HomeViewModel
import com.rishi.operater.ui.screens.session.SessionScreen
import com.rishi.operater.ui.screens.session.SessionViewModel
import com.rishi.operater.ui.screens.settings.SettingsScreen
import com.rishi.operater.ui.screens.settings.SettingsViewModel

@Composable
fun OperatoRNavHost() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            val navBackStackEntry = navController.currentBackStackEntryAsState().value
            val currentDestination = navBackStackEntry?.destination

            NavigationBar {
                topLevelDestinations.forEach { destination ->
                    val icon = when (destination) {
                        AppDestination.Home -> Icons.Default.Home
                        AppDestination.Session -> Icons.Default.PlayCircle
                        AppDestination.Settings -> Icons.Default.Settings
                    }

                    NavigationBarItem(
                        selected = currentDestination
                            ?.hierarchy
                            ?.any { it.route == destination.route } == true,
                        onClick = {
                            navController.navigate(destination.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(imageVector = icon, contentDescription = destination.label) },
                        label = { Text(text = destination.label) },
                    )
                }
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AppDestination.Home.route,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(route = AppDestination.Home.route) {
                val viewModel: HomeViewModel = viewModel()
                HomeScreen(uiState = viewModel.uiState)
            }
            composable(route = AppDestination.Session.route) {
                val viewModel: SessionViewModel = viewModel()
                SessionScreen(uiState = viewModel.uiState)
            }
            composable(route = AppDestination.Settings.route) {
                val viewModel: SettingsViewModel = viewModel()
                SettingsScreen(uiState = viewModel.uiState)
            }
        }
    }
}
