package com.example.coffeeapp.ui.screens.login

import android.app.Application
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import com.example.coffeeapp.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private fun getString(resId: Int): String = getApplication<Application>().getString(resId)

    fun onEmailChange(newEmail: String) {
        _uiState.update { it.copy(email = newEmail, errorMessage = "") }
    }

    fun onPasswordChange(newPassword: String) {
        _uiState.update { it.copy(password = newPassword, errorMessage = "") }
    }

    fun loginWithCredentials() {
        val email = _uiState.value.email.trim()
        val password = _uiState.value.password

        when {
            email.isEmpty() || password.isEmpty() -> {
                _uiState.update { it.copy(errorMessage = getString(R.string.error_empty_fields)) }
                return
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                _uiState.update { it.copy(errorMessage = getString(R.string.error_invalid_email)) }
                return
            }
            password.length < 6 -> {
                _uiState.update { it.copy(errorMessage = getString(R.string.error_weak_password)) }
                return
            }
        }

        _uiState.update { it.copy(isLoading = true) }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _uiState.update { it.copy(isLoading = false, isLoginSuccessful = true) }
                } else {
                    val friendlyMessage = getString(R.string.error_login_failed)
                    _uiState.update { it.copy(isLoading = false, errorMessage = friendlyMessage) }
                }
            }
    }

    fun resetNavigationState() {
        _uiState.update { it.copy(isLoginSuccessful = false) }
    }
}