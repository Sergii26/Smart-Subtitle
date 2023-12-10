package com.own.smartsubtitle.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.own.smartsubtitle.ui.screens.home.HomeScreen
import com.own.smartsubtitle.ui.screens.word.WordScreen


@Composable
fun NavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = "Home",
    ) {
        composable(route = NavDest.Home.route) {
            HomeScreen(navController)
        }
        composable(route = NavDest.Word.route) {
            WordScreen()
        }
    }
}

sealed class NavDest(val route: String) {
    data object Home: NavDest("home")
    data object Word: NavDest("wrod")
}
