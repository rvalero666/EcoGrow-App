package com.rvalero.ecogrow.data.remote.apiService.product

import com.rvalero.ecogrow.data.model.ApiResponseDto
import com.rvalero.ecogrow.data.model.product.ProductDetailResponseDto
import com.rvalero.ecogrow.data.model.product.ProductFeaturedResponseDto

interface ProductApiService {

    suspend fun getProductFeatured(): ApiResponseDto<List<ProductFeaturedResponseDto>>

    suspend fun searchProducts(query: String, limit: Int?): ApiResponseDto<List<ProductFeaturedResponseDto>>

    suspend fun getProductById(productId: Long): ApiResponseDto<ProductDetailResponseDto>
}