package com.example.ytclient

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ytclient.ui.HomeScreen
import com.example.ytclient.ui.PlayerScreen
import com.example.ytclient.ui.theme.YTClientTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            YTClientTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppNavHost()
                }
            }
        }
    }
}

@Composable
fun AppNavHost() {
    val navController: NavHostController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {

        composable("home") {
            HomeScreen(
                onVideoClick = { videoId ->
                    navController.navigate("player/$videoId")
                }
            )
        }

        composable(
            route = "player/{videoId}",
            arguments = listOf(navArgument("videoId") { type = NavType.StringType })
        ) { backStackEntry ->
            val videoId = backStackEntry.arguments?.getString("videoId") ?: ""
            PlayerScreen(
                videoId = videoId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
