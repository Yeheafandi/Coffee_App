package com.example.coffeeapp.ui.screens.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.coffeeapp.navigation.TempScreen
import com.example.coffeeapp.navigation.bottomNavItems
import com.example.coffeeapp.ui.screens.barista.BaristaScreen
import com.example.coffeeapp.ui.screens.home.HomeScreen
import com.example.coffeeapp.ui.screens.home.HomeViewModel
import com.example.coffeeapp.ui.screens.favorites.FavoritesScreen
import com.example.coffeeapp.ui.screens.profile.ProfileScreen
import com.example.coffeeapp.ui.theme.CoffeeBrownMain
import com.example.coffeeapp.ui.theme.CoffeeCreamBG

@Composable
fun MainScreen(
    rootNavController: NavController
) {
    val bottomNavController = rememberNavController()
    var selectedRoute by remember { mutableStateOf(com.example.coffeeapp.navigation.BottomNavItem.Home.route) }

    val sharedHomeViewModel: HomeViewModel = viewModel()

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                bottomNavItems.forEach { item ->
                    val isSelected = selectedRoute == item.route
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            selectedRoute = item.route
                            bottomNavController.navigate(item.route) {
                                popUpTo(bottomNavController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = stringResource(id = item.titleResId)
                            )
                        },
                        label = {
                            Text(text = stringResource(id = item.titleResId))
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = CoffeeBrownMain,
                            selectedTextColor = CoffeeBrownMain,
                            indicatorColor = CoffeeCreamBG
                        )
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = bottomNavController,
            startDestination = com.example.coffeeapp.navigation.BottomNavItem.Home.route,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            composable(com.example.coffeeapp.navigation.BottomNavItem.Home.route) {
                HomeScreen(
                    navController = rootNavController,
                    homeViewModel = sharedHomeViewModel
                )
            }

            composable(com.example.coffeeapp.navigation.BottomNavItem.AiBarista.route) {
                BaristaScreen()
            }

            composable(com.example.coffeeapp.navigation.BottomNavItem.Favorites.route) {
                FavoritesScreen(
                    navController = rootNavController,
                    homeViewModel = sharedHomeViewModel
                )
            }

            composable(com.example.coffeeapp.navigation.BottomNavItem.Profile.route) {
                ProfileScreen(navController = rootNavController)
            }
        }
    }
}