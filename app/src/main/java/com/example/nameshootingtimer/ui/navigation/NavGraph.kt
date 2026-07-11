package com.example.nameshootingtimer.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.nameshootingtimer.ui.screens.home.HomeScreen
import com.example.nameshootingtimer.ui.screens.routine.RoutineEditorScreen
import com.example.nameshootingtimer.ui.screens.execution.ExecutionScreen

@Composable
fun ShootingTimerNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(
                onNavigateToEditor = { navController.navigate("editor") },
                onNavigateToGallery = { /* TODO */ },
                onNavigateToSettings = { /* TODO */ },
                onNavigateToExecution = { navController.navigate("execution") }
            )
        }
        composable("editor") {
            RoutineEditorScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable("execution") {
            ExecutionScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
