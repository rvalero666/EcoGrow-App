package com.rvalero.ecogrow.ui.registerScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rvalero.ecogrow.data.remote.utils.NetworkResult
import com.rvalero.ecogrow.domain.model.Usuario
import com.rvalero.ecogrow.domain.useCase.RegisterUseCase
import com.rvalero.ecogrow.ui.util.UiEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterState())
    val uiState: StateFlow<RegisterState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private fun sendEvent(event: UiEvent) {
        viewModelScope.launch { _uiEvent.send(event) }
    }

    fun handleIntent(intent: RegisterIntent) {
        when (intent) {
            is RegisterIntent.NombreChanged -> _uiState.update { it.copy(nombre = intent.value) }
            is RegisterIntent.ApellidosChanged -> _uiState.update { it.copy(apellidos = intent.value) }
            is RegisterIntent.EmailChanged -> _uiState.update { it.copy(email = intent.value) }
            is RegisterIntent.PasswordChanged -> _uiState.update { it.copy(password = intent.value) }
            is RegisterIntent.ConfirmPasswordChanged -> _uiState.update { it.copy(confirmPassword = intent.value) }
            is RegisterIntent.TelefonoChanged -> _uiState.update { it.copy(telefono = intent.value) }
            is RegisterIntent.TogglePasswordVisibility -> _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
            is RegisterIntent.ToggleConfirmPasswordVisibility -> _uiState.update { it.copy(isConfirmPasswordVisible = !it.isConfirmPasswordVisible) }
            is RegisterIntent.Submit -> register()
        }
    }

    private fun register() {
        val state = _uiState.value
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val usuario = Usuario(
                nombre = state.nombre,
                apellidos = state.apellidos,
                email = state.email,
                password = state.password,
                telefono = state.telefono.ifBlank { null },
                rol = "consumidor"
            )
            val result = registerUseCase(usuario)
            _uiState.update { it.copy(isLoading = false) }
            when (result) {
                is NetworkResult.Success -> sendEvent(UiEvent.ShowSnackbar(result.data))
                is NetworkResult.Error -> sendEvent(UiEvent.ShowSnackbar(result.message ?: "Error inesperado"))
            }
        }
    }


}
