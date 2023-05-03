package com.dudqo.eventplanner.graphs

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.dudqo.eventplanner.screens.FriendsScreen
import com.dudqo.eventplanner.screens.settings.SettingsScreen
import com.dudqo.eventplanner.screens.events.EventCreateScreen
import com.dudqo.eventplanner.screens.events.EventViewScreen
import com.dudqo.eventplanner.screens.events.EventsScreen
import com.dudqo.eventplanner.screens.home.HomeScreen
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
        startDestination = EventScreen.CreateScreen.route + "?eventId={eventId}"
    ) {
        composable(
            route = EventScreen.CreateScreen.route + "?eventId={eventId}",
            arguments = listOf(
                navArgument(
                    name = "eventId"
                ) {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) {
            EventCreateScreen(navController)
        }
        composable(
            route = EventScreen.ViewScreen.route + "?eventId={eventId}",
            arguments = listOf(
                navArgument(
                    name = "eventId"
                ) {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) {
            EventViewScreen(navController)
        }
    }
}

sealed class EventScreen(val route: String) {
    object CreateScreen : EventScreen(route = "create_screen")
    object ViewScreen : EventScreen(route = "view_screen")
}