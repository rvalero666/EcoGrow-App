package com.rvalero.ecogrow.ui.loginScreen

sealed interface LoginIntent {
    data class EmailChanged(val value: String) : LoginIntent
    data class PasswordChanged(val value: String) : LoginIntent
    data object TogglePasswordVisibility : LoginIntent
    data object Submit : LoginIntent
    data object NavigateToRegister : LoginIntent
}
