package com.robertkomarek.novenen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.robertkomarek.novenen.ui.navigation.BottomNavItem
import com.robertkomarek.novenen.ui.navigation.BottomNavigationBar
import com.robertkomarek.novenen.view.BookmarksScreen
import com.robertkomarek.novenen.view.HomeScreen
import com.robertkomarek.novenen.view.ImpressumScreen
import com.robertkomarek.novenen.view.NotificationScreen
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.robertkomarek.novenen.ui.theme.Purple80
import com.robertkomarek.novenen.view.DetailScreenNovene

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            //Set statusbar background color
            val systemUiController = rememberSystemUiController()
            systemUiController.setStatusBarColor(color = Purple80)

            //Prevent the app's bottom navigator bar to be overlapped by device's navigation bar
            val navController = rememberNavController()

            Scaffold(
                bottomBar = { BottomNavigationBar(navController) }
            ) { innerPadding ->
                NavigationComponent(navController, innerPadding)
            }
        }
    }
}

@Composable
fun NavigationComponent(navController: NavHostController, innerPadding: PaddingValues) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.HomeScreen.screen_route,
        modifier = Modifier.padding(innerPadding)
    ) {
        composable(BottomNavItem.HomeScreen.screen_route) { HomeScreen(navController) }
        composable(BottomNavItem.BookmarksScreen.screen_route) { BookmarksScreen() }
        composable(BottomNavItem.ImpressumScreen.screen_route) { ImpressumScreen() }
        composable(BottomNavItem.NotificationScreen.screen_route) { NotificationScreen() }
        // ... other composable routes
        composable("details/{novenenname}") { backStackEntry ->
            val novenenname = backStackEntry.arguments?.getString("novenenname") ?: ""
            DetailScreenNovene(novenenname)
        }
    }
}

