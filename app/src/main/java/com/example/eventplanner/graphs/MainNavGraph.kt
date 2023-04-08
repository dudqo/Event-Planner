package com.example.eventplanner.graphs

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.eventplanner.screens.FriendsScreen
import com.example.eventplanner.SettingsScreen
import com.example.eventplanner.screens.EventCreateScreen
import com.example.eventplanner.screens.EventViewScreen
import com.example.eventplanner.screens.EventsScreen
import com.example.eventplanner.screens.HomeScreen

@ExperimentalMaterial3Api
@Composable
fun MainNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController = navController)
        }
        composable("events") {
            EventsScreen(navController = navController)
        }
        composable("friends") {
            FriendsScreen()
        }
        composable("settings") {
            SettingsScreen()
        }
        eventNavGraph(navController = navController)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.eventNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.EVENT,
        startDestination = EventScreen.CreateScreen.route
    ) {
        composable(route = EventScreen.CreateScreen.route) {
            EventCreateScreen()
        }
        composable(route = EventScreen.ViewScreen.route) {
            EventViewScreen()
        }
    }
}

sealed class EventScreen(val route: String) {
    object CreateScreen : EventScreen(route = "create_screen")
    object ViewScreen : EventScreen(route = "view_screen")
}