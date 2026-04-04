package com.rvalero.ecogrow.ui.loginScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rvalero.ecogrow.common.NetworkResult
import com.rvalero.ecogrow.domain.useCase.LoginUseCase
import com.rvalero.ecogrow.ui.util.navigation.Routes
import com.rvalero.ecogrow.ui.util.UiEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginState())
    val uiState: StateFlow<LoginState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private fun sendEvent(event: UiEvent) {
        viewModelScope.launch { _uiEvent.send(event) }
    }

    fun handleIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.EmailChanged -> _uiState.update { it.copy(email = intent.value) }
            is LoginIntent.PasswordChanged -> _uiState.update { it.copy(password = intent.value) }
            is LoginIntent.TogglePasswordVisibility -> _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
            is LoginIntent.Submit -> login()
            is LoginIntent.NavigateToRegister -> sendEvent(UiEvent.NavigateTo(Routes.RegisterRoute))
        }
    }

    private fun login() {
        val state = _uiState.value
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val result = loginUseCase(state.email, state.password)
            _uiState.update { it.copy(isLoading = false) }
            when (result) {
                is NetworkResult.Success -> {
                    sendEvent(UiEvent.ShowSnackbar("Login exitoso"))
                    sendEvent(UiEvent.NavigateTo(Routes.HomeRoute))
                }
                is NetworkResult.Error -> sendEvent(UiEvent.ShowSnackbar(result.message ?: "Error inesperado"))
            }
        }
    }
}
