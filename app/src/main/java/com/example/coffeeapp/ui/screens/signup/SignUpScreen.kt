package com.example.coffeeapp.ui.screens.signup


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
fun SignUpScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    signUpViewModel: SignUpViewModel = viewModel()
) {
    val uiState by signUpViewModel.uiState.collectAsState()

    // الاستماع لحدث النجاح للتنقل التلقائي إلى الشاشة الرئيسية (Home Screen)
    LaunchedEffect(uiState.isSignUpSuccessful) {
        if (uiState.isSignUpSuccessful) {
            navController.navigate(CoffeeScreen.Home.name) {
                popUpTo(CoffeeScreen.Login.name) { inclusive = false }
            }
            signUpViewModel.resetNavigationState()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(CoffeeCreamBG)
            .verticalScroll(rememberScrollState()), // دعم الشاشات الصغيرة منعاً لاختفاء العناصر
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
                text = stringResource(id = R.string.signup_title),
                fontSize = 30.sp,
                color = CoffeeBrownMain,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(bottom = 28.dp)
            )

            // حقل الاسم
            OutlinedTextField(
                value = uiState.name,
                onValueChange = { signUpViewModel.onNameChange(it) },
                label = { Text(stringResource(id = R.string.name_label)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = CoffeeBrownMain, focusedLabelColor = CoffeeBrownMain)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // حقل البريد الإلكتروني
            OutlinedTextField(
                value = uiState.email,
                onValueChange = { signUpViewModel.onEmailChange(it) },
                label = { Text(stringResource(id = R.string.email_label)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = CoffeeBrownMain, focusedLabelColor = CoffeeBrownMain)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // حقل كلمة المرور المطور
            OutlinedTextField(
                value = uiState.password,
                onValueChange = { signUpViewModel.onPasswordChange(it) },
                label = { Text(stringResource(id = R.string.password_label)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = CoffeeBrownMain, focusedLabelColor = CoffeeBrownMain)
            )

            // 🌟 عرض مؤشر ومقياس قوة كلمة المرور ديناميكياً (Unique Feature)
            if (uiState.password.isNotEmpty()) {
                val (labelText, indicatorColor) = when (uiState.passwordStrength) {
                    PasswordStrength.EMPTY -> "" to Color.Transparent
                    PasswordStrength.WEAK -> stringResource(R.string.password_weak) to Color.Red
                    PasswordStrength.MEDIUM -> stringResource(R.string.password_medium) to Color(0xFFFFA500)
                    PasswordStrength.STRONG -> stringResource(R.string.password_strong) to Color(0xFF2E7D32)
                }

                Column(modifier = Modifier.fillMaxWidth().padding(top = 6.dp)) {
                    Text(text = "قوة كلمة المرور: $labelText", color = indicatorColor, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = {
                            when (uiState.passwordStrength) {
                                PasswordStrength.EMPTY -> 0.0f
                                PasswordStrength.WEAK -> 0.33f
                                PasswordStrength.MEDIUM -> 0.66f
                                PasswordStrength.STRONG -> 1.0f
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(6.dp),
                        color = indicatorColor,
                        trackColor = Color.LightGray,
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // حقل تأكيد كلمة المرور
            OutlinedTextField(
                value = uiState.confirmPassword,
                onValueChange = { signUpViewModel.onConfirmPasswordChange(it) },
                label = { Text(stringResource(id = R.string.confirm_password_label)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = CoffeeBrownMain, focusedLabelColor = CoffeeBrownMain)
            )

            // رسائل الخطأ العامة
            if (uiState.errorMessage.isNotEmpty()) {
                Text(
                    text = uiState.errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 10.dp)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // زر التسجيل والتحميل
            if (uiState.isLoading) {
                CircularProgressIndicator(color = CoffeeBrownMain)
            } else {
                Button(
                    onClick = { signUpViewModel.registerNewUser() },
                    colors = ButtonDefaults.buttonColors(containerColor = CoffeeBrownMain),
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                ) {
                    Text(text = stringResource(id = R.string.signup_button), color = Color.White, fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = { navController.navigate(CoffeeScreen.Login.name) }) {
                Text(text = stringResource(id = R.string.go_to_login), color = CoffeeBrownMain)
            }
        }
    }
}