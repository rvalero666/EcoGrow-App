package com.rvalero.ecogrow.domain.repository

import com.rvalero.ecogrow.common.NetworkResult
import com.rvalero.ecogrow.domain.model.Product
import com.rvalero.ecogrow.domain.model.ProductDetail

interface ProductRepository {

    suspend fun getFeaturedProducts(): NetworkResult<List<Product>>

    suspend fun searchProducts(query: String, limit: Int?): NetworkResult<List<Product>>

    suspend fun getProductById(productId: Long): NetworkResult<ProductDetail>
}