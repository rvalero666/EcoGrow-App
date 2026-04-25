package com.rvalero.ecogrow.ui.profileScreen

import com.rvalero.ecogrow.domain.model.UserRole

data class ProfileState(
    val nombre: String = "",
    val rol: UserRole = UserRole.CONSUMER,
    val isLoading: Boolean = false,
    val error: String? = null
)
