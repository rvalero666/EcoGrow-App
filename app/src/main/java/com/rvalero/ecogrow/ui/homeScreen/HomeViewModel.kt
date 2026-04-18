package com.rvalero.ecogrow.ui.homeScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rvalero.ecogrow.common.NetworkResult
import com.rvalero.ecogrow.domain.useCase.product.GetFeaturedProductsUseCase
import com.rvalero.ecogrow.domain.useCase.producer.GetNearbyProducersUseCase
import com.rvalero.ecogrow.ui.util.UiEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal

class HomeViewModel(
    private val getNearbyProducersUseCase: GetNearbyProducersUseCase,
    private val getFeaturedProductsUseCase: GetFeaturedProductsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeState())
    val uiState: StateFlow<HomeState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        loadData()
    }

    private fun sendEvent(event: UiEvent) {
        viewModelScope.launch { _uiEvent.send(event) }
    }

    fun handleIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.LoadData -> loadData()
        }
    }

    private fun loadData() {
        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            launch {
                val result = getNearbyProducersUseCase(
                    latitud = BigDecimal("39.4699"),
                    longitud = BigDecimal("-0.3763"),
                    radioKm = 150,
                    limit = 5
                )
                when (result) {
                    is NetworkResult.Success -> _uiState.update { it.copy(producers = result.data) }
                    is NetworkResult.Error -> sendEvent(UiEvent.ShowSnackbar(result.message ?: "Error al cargar productores"))
                }
            }

            launch {
                val result = getFeaturedProductsUseCase()
                when (result) {
                    is NetworkResult.Success -> _uiState.update { it.copy(products = result.data) }
                    is NetworkResult.Error -> sendEvent(UiEvent.ShowSnackbar(result.message ?: "Error al cargar productos"))
                }
            }
        }.invokeOnCompletion {
            _uiState.update { it.copy(isLoading = false) }
        }
    }
}