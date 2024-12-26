package com.robertkomarek.novenen.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.robertkomarek.novenen.R


sealed class BottomNavItem(var title: String, var icon: @Composable () -> ImageVector, var screen_route: String) {

    object HomeScreen : BottomNavItem("Novenen", { BookmarkIcon(R.drawable.baseline_view_list_32) }, "novenen")
    object BiblePassageScreen : BottomNavItem("Bibelstelle", { BookmarkIcon(R.drawable.baseline_menu_book_32) }, "biblepassage")
    object SettingsScreen : BottomNavItem("Settings", { BookmarkIcon(R.drawable.baseline_info_outline_32) }, "bookmarks")
    object NotificationScreen :
        BottomNavItem("Erinnerung", { BookmarkIcon(R.drawable.baseline_notifications_32) }, "notification")
}

@Composable
fun BookmarkIcon(iconId:Int): ImageVector {
    return ImageVector.vectorResource(id = iconId)
}

