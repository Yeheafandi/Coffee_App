package com.example.coffeeapp.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember // 🌟 استيراد سطر الـ remember للحفاظ على مكدس الشاشة
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

// استيراد الواجهات المنظمة هندسياً تبعاً لـ MVVM
import com.example.coffeeapp.ui.splash.SplashScreen
import com.example.coffeeapp.ui.screens.login.LoginScreen
import com.example.coffeeapp.ui.screens.signup.SignUpScreen
import com.example.coffeeapp.ui.screens.main.MainScreen // الحاضنة الرئيسية للناف بار
import com.example.coffeeapp.ui.screens.detail.DetailScreen // استيراد شاشة التفاصيل الجديدة
import com.example.coffeeapp.ui.screens.home.HomeViewModel

// 1. تعريف مسارات الشاشات (تبعاً لـ Lab 8 / Navigation)
enum class CoffeeScreen {
    Splash,
    Login,
    SignUp,
    Home,
    AiBarista,
    Detail
}

// 2. إعداد محرك التنقل الرئيسي (NavHost) الخارجي لحماية دورة المصادقة
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = CoffeeScreen.Splash.name,
        modifier = Modifier.fillMaxSize()
    ) {
        // شاشة الـ Splash (ملء الشاشة)
        composable(route = CoffeeScreen.Splash.name) {
            SplashScreen(navController = navController)
        }

        // شاشة تسجيل الدخول (ملء الشاشة)
        composable(route = CoffeeScreen.Login.name) {
            LoginScreen(navController = navController)
        }

        // شاشة إنشاء الحساب المطور (ملء الشاشة)
        composable(route = CoffeeScreen.SignUp.name) {
            SignUpScreen(navController = navController)
        }

        // شاشة المتجر الرئيسية: توجه الآن حصراً إلى MainScreen لتفعيل الـ Bottom Navigation Bar
        composable(route = CoffeeScreen.Home.name) {
            MainScreen(rootNavController = navController)
        }

        // 🌟 3. مسار شاشة التفاصيل المطور (Detail Screen Route) مع المعاملات الديناميكية
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
            // استخراج القيم الممررة بأمان
            val title = backStackEntry.arguments?.getString("title") ?: ""
            val description = backStackEntry.arguments?.getString("description") ?: ""
            val price = backStackEntry.arguments?.getFloat("price") ?: 0.0f
            val rating = backStackEntry.arguments?.getFloat("rating") ?: 0.0f
            val extra = backStackEntry.arguments?.getString("extra") ?: ""

            // فك تشفير رابط الصورة القادم من الـ Firebase لمنع تعارض الرموز الخاصة
            val encodedPicUrl = backStackEntry.arguments?.getString("picUrl") ?: ""
            val picUrl = java.net.URLDecoder.decode(encodedPicUrl, "UTF-8")

            // 🌟 السطر السحري الذي كان ناقصاً في كودك للوصول إلى مكدس شاشة المتجر المفتوحة في الخلفية:
            val homeBackStackEntry = remember(backStackEntry) {
                navController.getBackStackEntry(CoffeeScreen.Home.name)
            }

            // الآن هذا السطر سيعرف المتغير بنجاح دون أي خطأ
            val sharedHomeViewModel: HomeViewModel = viewModel(homeBackStackEntry)

            // استدعاء شاشة التفاصيل الفاخرة وتمرير البيانات إليها لتعرض بملء الشاشة
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

// عنصر واجهة مؤقت لمنع أخطاء البناء أثناء التشغيل التجريبي للمراحل القادمة
@Composable
fun TempScreen(title: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = title, fontSize = 20.sp)
    }
}