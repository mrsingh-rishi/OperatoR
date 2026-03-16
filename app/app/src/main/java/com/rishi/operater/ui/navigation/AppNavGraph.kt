package com.rishi.operater.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rishi.operater.ui.home.HomeScreen
import com.rishi.operater.ui.session.SessionScreen
import com.rishi.operater.ui.settings.SettingsScreen

@Composable
fun AppNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = AppDestination.Home.route,
        modifier = modifier
    ) {
        composable(AppDestination.Home.route) {
            HomeScreen(
                onStartSession = { navController.navigate(AppDestination.Session.route) },
                onOpenSettings = { navController.navigate(AppDestination.Settings.route) }
            )
        }
        composable(AppDestination.Session.route) {
            SessionScreen(onBack = { navController.popBackStack() })
        }
        composable(AppDestination.Settings.route) {
            SettingsScreen(onBack = { navController.popBackStack() })
        }
    }
}
