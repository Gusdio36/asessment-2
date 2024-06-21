package com.d3if3089.flowernote.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.d3if3089.flowernote.ui.screen.DetailScreen
import com.d3if3089.flowernote.ui.screen.MainScreen

@Composable
fun SetUpnavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = Screen.Main.route
    ) {
        composable(Screen.Main.route) {
            MainScreen(navHostController = navController)
        }
        composable(Screen.AddNotes.route) {
            DetailScreen(navHostController = navController)
        }
        composable(
            Screen.EditNotes.route,
            arguments = listOf(
                navArgument(KEY_ID) {
                    type = NavType.IntType
                }
            )) {
            val id = it.arguments?.getInt(KEY_ID)
            DetailScreen(navHostController = navController, id)
        }
    }
}
