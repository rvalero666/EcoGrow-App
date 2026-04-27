package com.rvalero.ecogrow.ui.publishProductScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rvalero.ecogrow.common.NetworkResult
import com.rvalero.ecogrow.domain.model.NewProductDraft
import com.rvalero.ecogrow.domain.useCase.product.PublishProductUseCase
import com.rvalero.ecogrow.ui.util.UiEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PublishProductViewModel(
    private val publishProductUseCase: PublishProductUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PublishProductState())
    val uiState: StateFlow<PublishProductState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private fun sendEvent(event: UiEvent) {
        viewModelScope.launch { _uiEvent.send(event) }
    }

    fun handleIntent(intent: PublishProductIntent) {
        when (intent) {
            is PublishProductIntent.NombreChanged ->
                _uiState.update { it.copy(nombre = intent.value) }
            is PublishProductIntent.DescripcionChanged ->
                _uiState.update { it.copy(descripcion = intent.value) }
            is PublishProductIntent.CategoriaIdChanged ->
                _uiState.update { it.copy(categoriaId = intent.value) }
            is PublishProductIntent.PrecioChanged ->
                _uiState.update { it.copy(precio = intent.value) }
            is PublishProductIntent.UnidadChanged ->
                _uiState.update { it.copy(unidad = intent.value) }
            is PublishProductIntent.StockChanged ->
                _uiState.update { it.copy(stock = intent.value) }
            is PublishProductIntent.DisponibleChanged ->
                _uiState.update { it.copy(disponible = intent.value) }
            is PublishProductIntent.PhotosPicked -> _uiState.update {
                val merged = (it.photos + intent.uris)
                    .distinct()
                    .take(PublishProductOptions.MAX_PHOTOS)
                it.copy(photos = merged)
            }
            is PublishProductIntent.PhotoRemoved -> _uiState.update {
                it.copy(photos = it.photos - intent.uri)
            }
            is PublishProductIntent.Submit -> submit()
            is PublishProductIntent.NavigateBack ->
                sendEvent(UiEvent.NavigateBack)
        }
    }

    private fun submit() {
        val current = _uiState.value
        if (!current.isFormValid || current.isLoading) return
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = publishProductUseCase(
                draft = current.toDraft(),
                photoUris = current.photos
            )
            _uiState.update { it.copy(isLoading = false) }
            when (result) {
                is NetworkResult.Success -> {
                    sendEvent(UiEvent.ShowSnackbar("Producto publicado"))
                    _uiState.update { PublishProductState() }
                }
                is NetworkResult.Error ->
                    sendEvent(UiEvent.ShowSnackbar(result.message ?: "Error al publicar"))
            }
        }
    }
}

private fun PublishProductState.toDraft(): NewProductDraft = NewProductDraft(
    nombre = nombre.trim(),
    descripcion = descripcion.trim(),
    categoriaId = categoriaId,
    precio = precio.replace(',', '.').toDoubleOrNull() ?: 0.0,
    unidad = unidad,
    stock = stock.toIntOrNull() ?: 0,
    disponible = disponible
)
