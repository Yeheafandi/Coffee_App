package com.example.coffeeapp.ui.screens.signup

data class SignUpUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val errorMessage: String = "",
    val isLoading: Boolean = false,
    val isSignUpSuccessful: Boolean = false,
    val passwordStrength: PasswordStrength = PasswordStrength.EMPTY
)

enum class PasswordStrength {
    EMPTY, WEAK, MEDIUM, STRONG
}