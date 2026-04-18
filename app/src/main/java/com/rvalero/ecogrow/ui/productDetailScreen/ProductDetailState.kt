package com.rvalero.ecogrow.ui.productDetailScreen

import com.rvalero.ecogrow.domain.model.ProductDetail

data class ProductDetailState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val product: ProductDetail? = null,
    val quantity: Int = 1
)
