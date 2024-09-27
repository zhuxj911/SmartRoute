package com.xazhuxj.smartroute

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.xazhuxj.smartroute.ui.theme.SmartRouteTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartRouteTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController = rememberNavController()
                    SmartRouteAppNavHost(navController=navController, modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun SmartRouteAppNavHost(
    navController: NavHostController,
    modifier: Modifier
) {
    NavHost(navController, startDestination = "curveScreen") {
        composable("curveScreen") {
            CurveScreen(
                onNavigateToResultScreen = {
                    navController.navigate("resultScreen")
                },
                modifier = modifier
            )
        }

        composable("resultScreen") {
            ResultScreen(
                onNavigateToCurveScreen = { navController.navigate("curveScreen") },
                modifier = modifier
            )
        }
    }
}