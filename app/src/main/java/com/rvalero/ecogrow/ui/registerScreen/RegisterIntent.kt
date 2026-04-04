package com.rvalero.ecogrow.ui.registerScreen

sealed interface RegisterIntent {
    data class NombreChanged(val value: String) : RegisterIntent
    data class ApellidosChanged(val value: String) : RegisterIntent
    data class EmailChanged(val value: String) : RegisterIntent
    data class PasswordChanged(val value: String) : RegisterIntent
    data class ConfirmPasswordChanged(val value: String) : RegisterIntent
    data class TelefonoChanged(val value: String) : RegisterIntent
    data object TogglePasswordVisibility : RegisterIntent
    data object ToggleConfirmPasswordVisibility : RegisterIntent
    data object Submit : RegisterIntent
    data object NavigateToLogin : RegisterIntent
}
