package com.rvalero.ecogrow.ui.becomeProducerScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rvalero.ecogrow.common.NetworkResult
import com.rvalero.ecogrow.domain.useCase.producer.BecomeProducerUseCase
import com.rvalero.ecogrow.ui.util.UiEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BecomeProducerViewModel(
    private val becomeProducerUseCase: BecomeProducerUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(BecomeProducerState())
    val uiState: StateFlow<BecomeProducerState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private fun sendEvent(event: UiEvent) {
        viewModelScope.launch { _uiEvent.send(event) }
    }

    fun handleIntent(intent: BecomeProducerIntent) {
        when (intent) {
            is BecomeProducerIntent.NombreNegocioChanged ->
                _uiState.update { it.copy(nombreNegocio = intent.value) }
            is BecomeProducerIntent.DescripcionChanged ->
                _uiState.update { it.copy(descripcion = intent.value) }
            is BecomeProducerIntent.LocalidadChanged ->
                _uiState.update { it.copy(localidad = intent.value) }
            is BecomeProducerIntent.ProvinciaChanged ->
                _uiState.update { it.copy(provincia = intent.value) }
            is BecomeProducerIntent.Submit -> submit()
            is BecomeProducerIntent.NavigateBack -> sendEvent(UiEvent.NavigateBack)
        }
    }

    private fun submit() {
        val state = _uiState.value
        if (!state.isFormValid || state.isLoading) return

        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            val result = becomeProducerUseCase(
                nombreNegocio = state.nombreNegocio.trim(),
                descripcion = state.descripcion.trim().ifBlank { null },
                localidad = state.localidad.trim(),
                provincia = state.provincia.trim()
            )
            _uiState.update { it.copy(isLoading = false) }
            when (result) {
                is NetworkResult.Success -> {
                    sendEvent(UiEvent.NavigateBack)
                }
                is NetworkResult.Error -> {
                    sendEvent(UiEvent.ShowSnackbar(result.message ?: "Error inesperado"))
                }
            }
        }
    }
}
