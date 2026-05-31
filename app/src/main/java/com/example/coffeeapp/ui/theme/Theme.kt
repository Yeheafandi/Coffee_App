package com.example.coffeeapp.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

// 1. إعداد ألوان الوضع الداكن (Dark Mode) باستخدام درجات البني الدافئة
private val DarkColorScheme = darkColorScheme(
    primary = CoffeeBrownMain,
    background = androidx.compose.ui.graphics.Color(0xFF1C120C), // بني داكن جداً للخلفية
    surface = androidx.compose.ui.graphics.Color(0xFF2B1E16)
)

// 2. إعداد ألوان الوضع الفاتح (Light Mode) المتناسقة مع هوية التطبيق
private val LightColorScheme = lightColorScheme(
    primary = CoffeeBrownMain,
    background = CoffeeCreamBG, // الخلفية الكريمية المعتمدة في الشاشة الترحيبية
    surface = androidx.compose.ui.graphics.Color.White
)

@Composable
fun CoffeeAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // تعطيل الألوان الديناميكية الافتراضية لأندرويد 12+ للحفاظ على ألوان القهوة الخاصة بنا ثابتة وجميلة
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        // تأكد أن كلمة Typography غير حمراء، وإلا اتركها Typography الافتراضية
        content = content
    )
}