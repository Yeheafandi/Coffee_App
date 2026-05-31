package com.example.coffeeapp.ui.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.coffeeapp.R
import com.example.coffeeapp.navigation.CoffeeScreen
import com.example.coffeeapp.ui.theme.CoffeeBrownMain
import com.example.coffeeapp.ui.theme.CoffeeCreamBG
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController, modifier: Modifier = Modifier) {

    // دالة الانتقال التلقائي بعد ثانيتين تبعاً لمفهوم إدارة الأحداث الجانبية
    LaunchedEffect(key1 = true) {
        delay(2000)
        // الانتقال لشاشة الدخول وحذف شاشة الـ Splash من مكدس التنقل لكي لا يعود إليها المستخدم عند الضغط على زر الرجوع
        navController.navigate(CoffeeScreen.Login.name) {
            popUpTo(CoffeeScreen.Splash.name) { inclusive = true }
        }
    }

    // تصميم الواجهة بالاعتماد على المعدلات الحجمية والوزنية المأخوذة في المختبرات 2 و 3
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(CoffeeCreamBG), // الخلفية الكريمية للمتجر
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center // 🌟 تم الإصلاح هنا لتأخذ Arrangement بدلاً من Alignment
        ) {
            Text(
                text = "☕",
                fontSize = 80.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // استدعاء النص من strings.xml لمنع النصوص المباشرة (Hardcoded) نهائياً تماشياً مع معايير المختبر 3
            Text(
                text = stringResource(id = R.string.splash_welcome),
                color = CoffeeBrownMain,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}