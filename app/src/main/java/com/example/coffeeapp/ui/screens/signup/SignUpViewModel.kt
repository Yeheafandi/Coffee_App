package com.example.coffeeapp.ui.screens.signup

import android.app.Application
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import com.example.coffeeapp.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update





class SignUpViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState: StateFlow<SignUpUiState> = _uiState.asStateFlow()

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private fun getString(resId: Int): String = getApplication<Application>().getString(resId)

    fun onNameChange(newName: String) {
        _uiState.update { it.copy(name = newName, errorMessage = "") }
    }
    fun onEmailChange(newEmail: String) {
        _uiState.update { it.copy(email = newEmail, errorMessage = "") }
    }
    fun onConfirmPasswordChange(newConfirm: String) {
        _uiState.update { it.copy(confirmPassword = newConfirm, errorMessage = "") }
    }

    fun onPasswordChange(newPassword: String) {
        val strength = calculatePasswordStrength(newPassword)
        _uiState.update { it.copy(password = newPassword, passwordStrength = strength, errorMessage = "") }
    }

    private fun calculatePasswordStrength(password: String): PasswordStrength {
        if (password.isEmpty()) return PasswordStrength.EMPTY
        if (password.length < 6) return PasswordStrength.WEAK

        val hasLetters = password.any { it.isLetter() }
        val hasDigits = password.any { it.isDigit() }
        val hasSpecial = password.any { !it.isLetterOrDigit() }

        return when {
            hasLetters && hasDigits && hasSpecial && password.length >= 8 -> PasswordStrength.STRONG
            hasLetters && hasDigits -> PasswordStrength.MEDIUM
            else -> PasswordStrength.WEAK
        }
    }

    fun registerNewUser() {
        val state = _uiState.value
        val email = state.email.trim()
        val password = state.password
        val confirmPassword = state.confirmPassword

        when {
            state.name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() -> {
                _uiState.update { it.copy(errorMessage = getString(R.string.error_empty_fields)) }
                return
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                _uiState.update { it.copy(errorMessage = getString(R.string.error_invalid_email)) }
                return
            }
            password != confirmPassword -> {
                _uiState.update { it.copy(errorMessage = getString(R.string.error_password_mismatch)) }
                return
            }
            state.passwordStrength == PasswordStrength.WEAK -> {
                _uiState.update { it.copy(errorMessage = getString(R.string.error_weak_password)) }
                return
            }
        }

        _uiState.update { it.copy(isLoading = true) }
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _uiState.update { it.copy(isLoading = false, isSignUpSuccessful = true) }
                } else {
                    val serverError = task.exception?.localizedMessage ?: getString(R.string.error_unexpected)
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = serverError
                        )
                    }
                }
            }
    }

    fun resetNavigationState() { _uiState.update { it.copy(isSignUpSuccessful = false) } }
}