package com.kalivex.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kalivex.app.ui.screens.ChatScreen
import com.kalivex.app.ui.screens.DevicesScreen
import com.kalivex.app.ui.screens.HomeScreen
import com.kalivex.app.ui.screens.SettingsScreen
import com.kalivex.app.ui.screens.VoiceScreen
import com.kalivex.app.ui.screens.LoginScreen
import com.kalivex.app.network.ApiClient

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Voice : Screen("voice")
    object Chat : Screen("chat")
    object Settings : Screen("settings")
    object Devices : Screen("devices")
    object Login : Screen("login")
}

@Composable
fun NavGraph(navController: NavHostController = rememberNavController(), modifier: Modifier = Modifier) {
    // Hook auth expiry to navigate to login
    ApiClient.onAuthExpired = {
        try {
            // navigate on main thread
            navController.navigate(Screen.Login.route) {
                popUpTo(Screen.Home.route)
            }
        } catch (_: Exception) {}
    }

    NavHost(navController = navController, startDestination = Screen.Home.route, modifier = modifier) {
        composable(Screen.Home.route) { HomeScreen(onNavigate = { route -> navController.navigate(route) }) }
        composable(Screen.Voice.route) { VoiceScreen(onNavigateBack = { navController.popBackStack() }) }
        composable(Screen.Chat.route) { ChatScreen(onNavigateBack = { navController.popBackStack() }) }
        composable(Screen.Settings.route) { SettingsScreen(onNavigateBack = { navController.popBackStack() }) }
        composable(Screen.Devices.route) { DevicesScreen(onNavigateBack = { navController.popBackStack() }) }
        composable(Screen.Login.route) { LoginScreen(onNavigateBack = { navController.popBackStack() }, onLoginSuccess = { navController.popBackStack() }) }
    }
}
