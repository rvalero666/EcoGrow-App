package com.rvalero.ecogrow.domain.useCase.product

import com.rvalero.ecogrow.common.NetworkResult
import com.rvalero.ecogrow.domain.model.Product
import com.rvalero.ecogrow.domain.repository.ProductRepository

class GetFeaturedProductsUseCase(
    private val productRepository: ProductRepository
) {

    suspend operator fun invoke(): NetworkResult<List<Product>> {
        return productRepository.getFeaturedProducts()
    }
}