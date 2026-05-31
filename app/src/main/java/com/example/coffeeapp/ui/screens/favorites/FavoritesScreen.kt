package com.example.coffeeapp.ui.screens.favorites


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.coffeeapp.R
import com.example.coffeeapp.ui.screens.home.CoffeePopularCard
import com.example.coffeeapp.ui.screens.home.HomeViewModel
import com.example.coffeeapp.ui.theme.CoffeeBrownMain
import com.example.coffeeapp.ui.theme.CoffeeCreamBG
import com.example.coffeeapp.navigation.CoffeeScreen
import java.net.URLEncoder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    navController: NavController,
    homeViewModel: HomeViewModel // مشاركة نفس الـ ViewModel لقراءة البيانات المحدثة
) {
    val uiState by homeViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.nav_favorites), fontWeight = FontWeight.Bold, color = CoffeeBrownMain) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CoffeeCreamBG)
            )
        },
        containerColor = CoffeeCreamBG
    ) { innerPadding ->
        if (uiState.favoriteItems.isEmpty()) {
            // واجهة تظهر في حال كانت المفضلة فارغة
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.empty_favorites),
                    fontSize = 16.sp,
                    color = CoffeeBrownMain.copy(alpha = 0.6f)
                )
            }
        } else {
            // عرض العناصر المفضلة باستخدام الكروت المجهزة هندسياً
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiState.favoriteItems) { coffeeItem ->
                    CoffeePopularCard(
                        coffeeItem = coffeeItem,
                        onClick = {
                            // تفعيل إمكانية الضغط والدخول للتفاصيل من شاشة المفضلة أيضاً!
                            val encodedUrl = URLEncoder.encode(coffeeItem.picUrl, "UTF-8")
                            navController.navigate(
                                "${CoffeeScreen.Detail.name}/${coffeeItem.title}/${coffeeItem.description}/${coffeeItem.price.toFloat()}/${coffeeItem.rating.toFloat()}/${coffeeItem.extra}/$encodedUrl"
                            )
                        }
                    )
                }
            }
        }
    }
}