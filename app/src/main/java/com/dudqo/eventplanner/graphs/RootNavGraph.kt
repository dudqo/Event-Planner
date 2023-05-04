package com.dudqo.eventplanner.graphs

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dudqo.eventplanner.screens.BottomNavBarScreen
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun RootNavGraph(navController: NavHostController) {
    val auth = Firebase.auth

    NavHost(
        navController = navController,
        route = Graph.ROOT,
        startDestination = if (auth.currentUser == null) Graph.AUTH else Graph.MAIN
    ) {
        authNavGraph(navController = navController)
        composable(route = Graph.MAIN) {
            BottomNavBarScreen()
        }
    }
}

object Graph {
    const val ROOT = "root_graph"
    const val AUTH = "auth_graph"
    const val MAIN = "main_graph"
    const val EVENT = "event_graph"
    const val SETTINGS = "settings_graph"
}