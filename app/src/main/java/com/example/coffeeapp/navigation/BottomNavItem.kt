package com.example.coffeeapp.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Face
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.coffeeapp.R

sealed class BottomNavItem(
    val route: String,
    val titleResId: Int,
    val icon: ImageVector
) {
    object Home : BottomNavItem("home_tab", R.string.nav_home, Icons.Default.Home)
    object AiBarista : BottomNavItem("barista_tab", R.string.nav_barista, Icons.Default.Face)
    object Favorites : BottomNavItem("favorites_tab", R.string.nav_favorites, Icons.Default.Favorite)
    object Profile : BottomNavItem("profile_tab", R.string.nav_profile, Icons.Default.Person)
}

val bottomNavItems = listOf(
    BottomNavItem.Home,
    BottomNavItem.AiBarista,
    BottomNavItem.Favorites,
    BottomNavItem.Profile
)