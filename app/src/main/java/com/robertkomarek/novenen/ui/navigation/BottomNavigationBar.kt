package com.robertkomarek.novenen.ui.navigation

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.robertkomarek.novenen.ui.theme.Pink80

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem.HomeScreen,
        BottomNavItem.BiblePassageScreen,
        BottomNavItem.NotificationScreen,
        BottomNavItem.SettingsScreen,
    )
    NavigationBar ( // Use NavigationBar from material3 instead of BottomNavigation
        containerColor = Pink80,
        contentColor = Color.White,
        modifier = Modifier.navigationBarsPadding()
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currenRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            NavigationBarItem( // use NavigationBarItem instead of BottomNavigationItem
                icon = { Icon(imageVector = item.icon(), contentDescription = item.title) },
                selected = currenRoute == item.screen_route,
                onClick = {
                    navController.navigate(item.screen_route) {
                        // Pop up to the root destination of the graph to avoid building up a large
                        // stack of destinations on the back stack as users select items
                        navController.graph.startDestinationRoute?.let {
                            screen_route ->
                                popUpTo(screen_route) {
                                    saveState = true
                            }
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                }
            )
        }
    }
}