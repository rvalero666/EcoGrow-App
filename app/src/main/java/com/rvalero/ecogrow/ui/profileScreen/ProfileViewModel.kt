package com.rvalero.ecogrow.ui.profileScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rvalero.ecogrow.common.NetworkResult
import com.rvalero.ecogrow.domain.useCase.auth.GetCurrentUserUseCase
import com.rvalero.ecogrow.domain.useCase.auth.LogoutUseCase
import com.rvalero.ecogrow.ui.util.UiEvent
import com.rvalero.ecogrow.ui.util.navigation.Routes
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileState())
    val uiState: StateFlow<ProfileState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        observeCurrentUser()
    }

    private fun observeCurrentUser() {
        viewModelScope.launch {
            getCurrentUserUseCase().collect { user ->
                _uiState.update {
                    it.copy(
                        nombre = user.nombre.orEmpty(),
                        rol = user.rol
                    )
                }
            }
        }
    }

    private fun sendEvent(event: UiEvent) {
        viewModelScope.launch { _uiEvent.send(event) }
    }

    fun handleIntent(intent: ProfileIntent) {
        when (intent) {
            is ProfileIntent.BecomeProducerClicked ->
                sendEvent(UiEvent.NavigateTo(Routes.BecomeProducerRoute))
            is ProfileIntent.LogoutClicked ->
                _uiState.update { it.copy(showLogoutDialog = true) }
            is ProfileIntent.LogoutDismissed ->
                _uiState.update { it.copy(showLogoutDialog = false) }
            is ProfileIntent.LogoutConfirmed -> logout()
            else -> {
                // Resto de opciones (pedidos, favoritos, editar perfil, etc.) sin implementar aún
            }
        }
    }

    private fun logout() {
        _uiState.update { it.copy(showLogoutDialog = false, isLoading = true) }
        viewModelScope.launch {
            val result = logoutUseCase()
            _uiState.update { it.copy(isLoading = false) }
            when (result) {
                is NetworkResult.Success ->
                    sendEvent(UiEvent.NavigateTo(Routes.LoginRoute))
                is NetworkResult.Error ->
                    sendEvent(UiEvent.ShowSnackbar(result.message ?: "Error al cerrar sesión"))
            }
        }
    }
}
