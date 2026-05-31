package com.example.coffeeapp.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.coffeeapp.R
import com.example.coffeeapp.navigation.CoffeeScreen
import com.example.coffeeapp.ui.theme.CoffeeBrownMain
import com.example.coffeeapp.ui.theme.CoffeeCreamBG
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController
) {
    val currentUser = remember { FirebaseAuth.getInstance().currentUser }
    val userName = currentUser?.displayName ?: "مطور القهوة الفاخرة"
    val userEmail = currentUser?.email ?: "developer@coffeeapp.com"

    // 🌟 متغير الحالة للتحكم في ظهور نافذة تأكيد تسجيل الخروج
    var showLogoutDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.nav_profile), fontWeight = FontWeight.Bold, color = CoffeeBrownMain) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CoffeeCreamBG)
            )
        },
        containerColor = CoffeeCreamBG
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // 1. كارت الصورة الشخصية والاسم
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(CoffeeBrownMain.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile Picture",
                    tint = CoffeeBrownMain,
                    modifier = Modifier.size(54.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(text = userName, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = CoffeeBrownMain)
            Text(text = userEmail, fontSize = 14.sp, color = Color.Gray, modifier = Modifier.padding(top = 4.dp))

            Spacer(modifier = Modifier.height(32.dp))

            // 2. قائمة الخيارات والإعدادات الذكية
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(vertical = 8.dp)) {
                    ProfileMenuItem(icon = Icons.Default.ShoppingCart, title = stringResource(R.string.my_orders)) {}
                    ProfileMenuItem(icon = Icons.Default.Notifications, title = stringResource(R.string.notifications)) {}
                    ProfileMenuItem(icon = Icons.Default.Settings, title = stringResource(R.string.app_settings)) {}
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 3. زر تسجيل الخروج الفاخر (Sign Out)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        // 🌟 فتح النافذة الحوارية عند الضغط بدلاً من تسجيل الخروج المباشر
                        showLogoutDialog = true
                    },
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = stringResource(R.string.sign_out),
                            tint = Color.Red
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(text = stringResource(R.string.sign_out), color = Color.Red, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                    Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = Color.LightGray)
                }
            }
        }
    }

    // 🌟 4. منطق بناء الـ AlertDialog لتأكيد الخروج
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false }, // إغلاق النافذة عند الضغط خارجها
            title = {
                Text(
                    text = stringResource(R.string.logout_confirm_title),
                    fontWeight = FontWeight.Bold,
                    color = CoffeeBrownMain,
                    fontSize = 18.sp
                )
            },
            text = {
                Text(
                    text = stringResource(R.string.logout_confirm_message),
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false // إغلاق النافذة أولاً
                        FirebaseAuth.getInstance().signOut() // تسجيل الخروج من Firebase
                        navController.navigate(CoffeeScreen.Login.name) {
                            popUpTo(0) { inclusive = true } // مسح المكدس والعودة لـ Login
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red) // لون أحمر للتنبيه بالخروج
                ) {
                    Text(text = stringResource(R.string.logout_yes), color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showLogoutDialog = false } // إغلاق النافذة والبقاء في الحساب
                ) {
                    Text(text = stringResource(R.string.cancel_button), color = Color.Gray, fontWeight = FontWeight.Medium)
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(20.dp)
        )
    }
}

@Composable
fun ProfileMenuItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icon, contentDescription = title, tint = CoffeeBrownMain)
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = title, fontSize = 15.sp, fontWeight = FontWeight.Medium, color = Color.DarkGray)
        }
        Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = Color.LightGray)
    }
}