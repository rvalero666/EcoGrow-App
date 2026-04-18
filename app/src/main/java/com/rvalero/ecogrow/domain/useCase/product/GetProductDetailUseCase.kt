package com.rvalero.ecogrow.domain.useCase.product

import com.rvalero.ecogrow.common.NetworkResult
import com.rvalero.ecogrow.domain.model.ProductDetail
import com.rvalero.ecogrow.domain.repository.ProductRepository

class GetProductDetailUseCase(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(productId: Long): NetworkResult<ProductDetail> {
        return productRepository.getProductById(productId)
    }
}
