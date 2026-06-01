package com.example.coffeeapp.ui.screens.login

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val errorMessage: String = "",
    val isLoading: Boolean = false,
    val isLoginSuccessful: Boolean = false
)
