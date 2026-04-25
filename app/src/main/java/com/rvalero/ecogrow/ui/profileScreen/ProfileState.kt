package com.rvalero.ecogrow.ui.profileScreen

import com.rvalero.ecogrow.domain.model.UserRole

data class ProfileState(
    val nombre: String = "",
    val email: String = "",
    val rol: UserRole = UserRole.CONSUMER,
    val nombreNegocio: String? = null,
    val isLoading: Boolean = false,
    val showLogoutDialog: Boolean = false
)
