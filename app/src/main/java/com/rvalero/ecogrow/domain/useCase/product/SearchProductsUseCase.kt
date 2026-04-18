package com.rvalero.ecogrow.domain.useCase.product

import com.rvalero.ecogrow.common.NetworkResult
import com.rvalero.ecogrow.domain.model.Product
import com.rvalero.ecogrow.domain.repository.ProductRepository

class SearchProductsUseCase(
    private val productRepository: ProductRepository
) {

    suspend operator fun invoke(query: String, limit: Int? = null): NetworkResult<List<Product>> {
        return productRepository.searchProducts(query, limit)
    }
}
