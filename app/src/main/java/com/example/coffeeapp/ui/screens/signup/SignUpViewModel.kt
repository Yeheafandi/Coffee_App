package com.example.coffeeapp.ui.screens.signup


import android.util.Patterns
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// 1. تجميع حالة واجهة إنشاء الحساب في كائن موحد (UI State) تماشياً مع Lab 6
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

// دالة مخصصة لتقييم قوة كلمة المرور (Unique Feature)
enum class PasswordStrength {
    EMPTY, WEAK, MEDIUM, STRONG
}

class SignUpViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState: StateFlow<SignUpUiState> = _uiState.asStateFlow()

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun onNameChange(newName: String) { _uiState.update { it.copy(name = newName, errorMessage = "") } }
    fun onEmailChange(newEmail: String) { _uiState.update { it.copy(email = newEmail, errorMessage = "") } }
    fun onConfirmPasswordChange(newConfirm: String) { _uiState.update { it.copy(confirmPassword = newConfirm, errorMessage = "") } }

    // فحص قوة كلمة المرور ديناميكياً عند كل حرف يكتبه المستخدم (Reactive State)
    fun onPasswordChange(newPassword: String) {
        val strength = calculatePasswordStrength(newPassword)
        _uiState.update { it.copy(password = newPassword, passwordStrength = strength, errorMessage = "") }
    }

    private fun calculatePasswordStrength(password: String): PasswordStrength {
        if (password.isEmpty()) return PasswordStrength.EMPTY
        if (password.length < 6) return PasswordStrength.WEAK

        // استخدام الـ Regex المتقدم للفحص الاحترافي
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

        // منطق التحقق الصارم من المدخلات والمطابقة (Validation Logic)
        when {
            state.name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() -> {
                _uiState.update { it.copy(errorMessage = "عذراً، لا يمكن ترك الحقول فارغة!") }
                return
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                _uiState.update { it.copy(errorMessage = "صيغة البريد الإلكتروني غير صحيحة!") }
                return
            }
            password != confirmPassword -> {
                _uiState.update { it.copy(errorMessage = "كلمتا المرور غير متطابقتين!") }
                return
            }
            state.passwordStrength == PasswordStrength.WEAK -> {
                _uiState.update { it.copy(errorMessage = "يرجى اختيار كلمة مرور أقوى لحماية حسابك.") }
                return
            }
        }

        // إرسال البيانات إلى Firebase Authentication
        _uiState.update { it.copy(isLoading = true) }
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _uiState.update { it.copy(isLoading = false, isSignUpSuccessful = true) }
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "فشل إنشاء الحساب: ${task.exception?.localizedMessage}"
                        )
                    }
                }
            }
    }

    fun resetNavigationState() { _uiState.update { it.copy(isSignUpSuccessful = false) } }
}