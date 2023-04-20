package com.dudqo.eventplanner

import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val name: String,
    val route: String,
    val icon_default: ImageVector,
    val icon_selected: ImageVector,
    val badge: Boolean = false
)
