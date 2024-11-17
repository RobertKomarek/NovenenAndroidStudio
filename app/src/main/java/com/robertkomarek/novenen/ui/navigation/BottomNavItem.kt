package com.robertkomarek.novenen.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(var title: String, var icon: ImageVector, var screen_route: String) {
    object HomeScreen : BottomNavItem("Novenen", Icons.Default.Home, "novenen")
    object BookmarksScreen : BottomNavItem("Lesezeichen", Icons.Default.Favorite, "bookmarks")
    object NotificationScreen : BottomNavItem("Erinnerung", Icons.Default.Notifications, "notification")
    object ImpressumScreen : BottomNavItem("Bibelstelle", Icons.Default.Person, "biblepassage")

}