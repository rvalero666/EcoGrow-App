package com.rvalero.ecogrow.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rvalero.ecogrow.domain.model.UserRole
import com.rvalero.ecogrow.domain.useCase.auth.GetCurrentUserUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class MainViewModel(
    getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    val userRole: StateFlow<UserRole> = getCurrentUserUseCase()
        .map { it.rol }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UserRole.CONSUMER
        )
}
