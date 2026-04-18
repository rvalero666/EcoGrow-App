package com.rvalero.ecogrow.ui.productDetailScreen

sealed interface ProductDetailIntent {
    data object IncrementQuantity : ProductDetailIntent
    data object DecrementQuantity : ProductDetailIntent
    data object AddToCart : ProductDetailIntent
    data object NavigateBack : ProductDetailIntent
}
