package com.example.coffeeapp.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.coffeeapp.ui.splash.SplashScreen
import com.example.coffeeapp.ui.screens.login.LoginScreen
import com.example.coffeeapp.ui.screens.signup.SignUpScreen
import com.example.coffeeapp.ui.screens.main.MainScreen
import com.example.coffeeapp.ui.screens.detail.DetailScreen
import com.example.coffeeapp.ui.screens.home.HomeViewModel

enum class CoffeeScreen {
    Splash,
    Login,
    SignUp,
    Home,
    Detail
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = CoffeeScreen.Splash.name,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(route = CoffeeScreen.Splash.name) {
            SplashScreen(navController = navController)
        }

        composable(route = CoffeeScreen.Login.name) {
            LoginScreen(navController = navController)
        }

        composable(route = CoffeeScreen.SignUp.name) {
            SignUpScreen(navController = navController)
        }

        composable(route = CoffeeScreen.Home.name) {
            MainScreen(rootNavController = navController)
        }

        composable(
            route = "${CoffeeScreen.Detail.name}/{title}/{description}/{price}/{rating}/{extra}/{picUrl}",
            arguments = listOf(
                navArgument("title") { type = NavType.StringType },
                navArgument("description") { type = NavType.StringType },
                navArgument("price") { type = NavType.FloatType },
                navArgument("rating") { type = NavType.FloatType },
                navArgument("extra") { type = NavType.StringType },
                navArgument("picUrl") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val title = backStackEntry.arguments?.getString("title") ?: ""
            val description = backStackEntry.arguments?.getString("description") ?: ""
            val price = backStackEntry.arguments?.getFloat("price") ?: 0.0f
            val rating = backStackEntry.arguments?.getFloat("rating") ?: 0.0f
            val extra = backStackEntry.arguments?.getString("extra") ?: ""

            val encodedPicUrl = backStackEntry.arguments?.getString("picUrl") ?: ""
            val picUrl = java.net.URLDecoder.decode(encodedPicUrl, "UTF-8")

            val homeBackStackEntry = remember(backStackEntry) {
                navController.getBackStackEntry(CoffeeScreen.Home.name)
            }

            val sharedHomeViewModel: HomeViewModel = viewModel(homeBackStackEntry)

            DetailScreen(
                navController = navController,
                title = title,
                description = description,
                price = price.toDouble(),
                rating = rating.toDouble(),
                extra = extra,
                picUrl = picUrl,
                homeViewModel = sharedHomeViewModel
            )
        }
    }
}

@Composable
fun TempScreen(title: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = title, fontSize = 20.sp)
    }
}