package com.example.coffeeapp.ui.screens.login


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.coffeeapp.R
import com.example.coffeeapp.navigation.CoffeeScreen
import com.example.coffeeapp.ui.theme.CoffeeBrownMain
import com.example.coffeeapp.ui.theme.CoffeeCreamBG

@Composable
fun LoginScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    loginViewModel: LoginViewModel = viewModel() // استدعاء تلقائي للـ ViewModel المعماري
) {
    // مراقبة التغيرات في الحالة البرمجية وتحويلها إلى Compose State
    val uiState by loginViewModel.uiState.collectAsState()

    // الاستماع لحدث النجاح للانتقال الآمن بين الشاشات
    LaunchedEffect(uiState.isLoginSuccessful) {
        if (uiState.isLoginSuccessful) {
            navController.navigate(CoffeeScreen.Home.name) {
                popUpTo(CoffeeScreen.Login.name) { inclusive = true }
            }
            loginViewModel.resetNavigationState()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(CoffeeCreamBG),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.login_title),
                fontSize = 32.sp,
                color = CoffeeBrownMain,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(bottom = 36.dp)
            )

            // حقل إدخال البريد الإلكتروني
            OutlinedTextField(
                value = uiState.email,
                onValueChange = { loginViewModel.onEmailChange(it) },
                label = { Text(stringResource(id = R.string.email_label)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CoffeeBrownMain,
                    focusedLabelColor = CoffeeBrownMain
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // حقل إدخال كلمة المرور
            OutlinedTextField(
                value = uiState.password,
                onValueChange = { loginViewModel.onPasswordChange(it) },
                label = { Text(stringResource(id = R.string.password_label)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CoffeeBrownMain,
                    focusedLabelColor = CoffeeBrownMain
                )
            )

            // عرض الأخطاء ديناميكياً بأسلوب منسق
            if (uiState.errorMessage.isNotEmpty()) {
                Text(
                    text = uiState.errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 10.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // التحكم بزر الدخول بناءً على حالة التحميل من السيرفر
            if (uiState.isLoading) {
                CircularProgressIndicator(color = CoffeeBrownMain)
            } else {
                Button(
                    onClick = { loginViewModel.loginWithCredentials() },
                    colors = ButtonDefaults.buttonColors(containerColor = CoffeeBrownMain),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.login_button),
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            TextButton(
                onClick = { navController.navigate(CoffeeScreen.SignUp.name) }
            ) {
                Text(
                    text = stringResource(id = R.string.go_to_signup),
                    color = CoffeeBrownMain,
                    fontSize = 14.sp
                )
            }
        }
    }
}