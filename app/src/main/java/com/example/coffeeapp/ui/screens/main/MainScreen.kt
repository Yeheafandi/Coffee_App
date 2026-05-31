package com.example.coffeeapp.ui.screens.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel // استيراد دالة الـ viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.coffeeapp.navigation.TempScreen
import com.example.coffeeapp.navigation.bottomNavItems
import com.example.coffeeapp.ui.screens.barista.BaristaScreen
import com.example.coffeeapp.ui.screens.home.HomeScreen
import com.example.coffeeapp.ui.screens.home.HomeViewModel // استيراد الـ ViewModel المشترك
import com.example.coffeeapp.ui.screens.favorites.FavoritesScreen // استيراد شاشة المفضلة الحقيقية
import com.example.coffeeapp.ui.screens.profile.ProfileScreen
import com.example.coffeeapp.ui.theme.CoffeeBrownMain
import com.example.coffeeapp.ui.theme.CoffeeCreamBG

@Composable
fun MainScreen(
    rootNavController: NavController // متحكم التنقل الرئيسي للتطبيق ككل
) {
    // محرك تنقل داخلي خاص فقط بالشاشات الأربعة السفلية
    val bottomNavController = rememberNavController()
    var selectedRoute by remember { mutableStateOf(com.example.coffeeapp.navigation.BottomNavItem.Home.route) }

    // 🌟 إنشاء نسخة واحدة (Single Instance) من الـ HomeViewModel لتشاركها الشاشات السفلية والتفاصيل
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
            // 1. تبويب الشاشة الرئيسية (نمرر لها الـ ViewModel المشترك)
            composable(com.example.coffeeapp.navigation.BottomNavItem.Home.route) {
                HomeScreen(
                    navController = rootNavController,
                    homeViewModel = sharedHomeViewModel
                )
            }

            // 2. تبويب باريستا الـ AI
            composable(com.example.coffeeapp.navigation.BottomNavItem.AiBarista.route) {
                BaristaScreen()
            }

            // 3. 🌟 تبويب المفضلة الحقيقي مربوط الآن بالشاشة الحقيقية ونفس الـ ViewModel
            composable(com.example.coffeeapp.navigation.BottomNavItem.Favorites.route) {
                FavoritesScreen(
                    navController = rootNavController,
                    homeViewModel = sharedHomeViewModel
                )
            }

            // 4. تبويب الحساب الشخصي (Profile)
            composable(com.example.coffeeapp.navigation.BottomNavItem.Profile.route) {
                ProfileScreen(navController = rootNavController) // تمرير الـ rootNavController للعودة لشاشة الـ Login
            }
        }
    }
}