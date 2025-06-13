package com.faiqathifnurrahimhadiko607062330082.assessment3.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.faiqathifnurrahimhadiko607062330082.assessment3.ui.screen.HomeScreen
import com.faiqathifnurrahimhadiko607062330082.assessment3.ui.screen.MainScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val screens = listOf(
        Screen.Home,
        Screen.Main
    )

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController, items = screens)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) { HomeScreen() }
            composable(Screen.Main.route) { MainScreen() }
        }
    }
}