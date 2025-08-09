package com.example.scanwithonline.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.scanwithonline.R // You'll need to add a logo to your drawable resources
import com.example.scanwithonline.ui.theme.ScanWithOnlineTheme
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    // This will run once when the screen is shown.
    LaunchedEffect(key1 = true) {
        delay(2000L) // Wait for 2 seconds
        // After 2 seconds, it will try to go to the "home_screen".
        // We will create this screen in the next step.
        navController.popBackStack()
        navController.navigate("home_screen")
    }

    // This is the visual layout of the splash screen.
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {
        // You can replace this with your app's logo.
        // For now, it uses the default Android launcher icon.
        Icon(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "App Logo",
            tint = Color.White
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    ScanWithOnlineTheme {
        SplashScreen(navController = rememberNavController())
    }
}
