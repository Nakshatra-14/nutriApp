package com.example.scanwithonline.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.scanwithonline.ui.screens.*

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "splash_screen") {
        composable("splash_screen") {
            SplashScreen(navController = navController)
        }
        composable("home_screen") {
            HomeScreen(navController = navController)
        }
        composable("scanner_screen") {
            // The ScannerScreen now takes the NavController directly.
            ScannerScreen(navController = navController)
        }
        composable("manual_input_screen") {
            ManualInputScreen(navController = navController)
        }
        composable(
            route = "details_screen/{barcode}",
            arguments = listOf(navArgument("barcode") { type = NavType.StringType })
        ) { backStackEntry ->
            val barcode = backStackEntry.arguments?.getString("barcode")
            if (barcode != null) {
                DetailsScreen(barcode = barcode, navController = navController)
            }
        }
        composable("score_info_screen") {
            ScoreInfoScreen(navController = navController)
        }
        composable("gi_info_screen") {
            GiInfoScreen(navController = navController)
        }
        composable("diabetes_guidance_screen") {
            DiabetesGuidanceScreen(navController = navController)
        }
    }
}
