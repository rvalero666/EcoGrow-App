package com.rvalero.ecogrow.ui.loginScreen

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val isPasswordVisible: Boolean = false
)
