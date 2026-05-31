package com.example.coffeeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.coffeeapp.navigation.AppNavigation
import com.example.coffeeapp.ui.theme.CoffeeAppTheme // 🌟 استيراد الثيم الصحيح هنا

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // استدعاء الثيم المحدث الخاص بـ coffeeapp ليعمل بشكل متناسق مع الألوان الجديدة
            CoffeeAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // استدعاء نظام التنقل الرئيسي
                    AppNavigation()
                }
            }
        }
    }
}