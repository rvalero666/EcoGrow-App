package com.rvalero.ecogrow.ui.productDetailScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rvalero.ecogrow.common.NetworkResult
import com.rvalero.ecogrow.domain.useCase.product.GetProductDetailUseCase
import com.rvalero.ecogrow.ui.util.UiEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductDetailViewModel(
    private val productId: Long,
    private val getProductDetailUseCase: GetProductDetailUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductDetailState())
    val uiState: StateFlow<ProductDetailState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        loadProduct()
    }

    private fun sendEvent(event: UiEvent) {
        viewModelScope.launch { _uiEvent.send(event) }
    }

    fun handleIntent(intent: ProductDetailIntent) {
        when (intent) {
            is ProductDetailIntent.IncrementQuantity -> incrementQuantity()
            is ProductDetailIntent.DecrementQuantity -> decrementQuantity()
            is ProductDetailIntent.AddToCart -> addToCart()
            is ProductDetailIntent.NavigateBack -> sendEvent(UiEvent.NavigateBack)
        }
    }

    private fun loadProduct() {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            when (val result = getProductDetailUseCase(productId)) {
                is NetworkResult.Success -> _uiState.update {
                    it.copy(isLoading = false, product = result.data)
                }
                is NetworkResult.Error -> {
                    _uiState.update { it.copy(isLoading = false, error = result.message) }
                    sendEvent(UiEvent.ShowSnackbar(result.message ?: "Error al cargar el producto"))
                }
            }
        }
    }

    private fun incrementQuantity() {
        val product = _uiState.value.product ?: return
        val maxStock = if (product.stock > 0) product.stock else Int.MAX_VALUE
        _uiState.update {
            it.copy(quantity = (it.quantity + 1).coerceAtMost(maxStock))
        }
    }

    private fun decrementQuantity() {
        _uiState.update {
            it.copy(quantity = (it.quantity - 1).coerceAtLeast(1))
        }
    }

    private fun addToCart() {
        val product = _uiState.value.product ?: return
        val qty = _uiState.value.quantity
        sendEvent(UiEvent.ShowSnackbar("${product.nombre} x$qty añadido a la cesta"))
    }
}
