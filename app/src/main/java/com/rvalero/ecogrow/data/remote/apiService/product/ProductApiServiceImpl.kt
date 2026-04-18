package com.rvalero.ecogrow.data.remote.apiService.product

import com.rvalero.ecogrow.data.model.ApiResponseDto
import com.rvalero.ecogrow.data.model.product.ProductFeaturedResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class ProductApiServiceImpl (
    private val client: HttpClient
): ProductApiService{


    override suspend fun getProductFeatured(): ApiResponseDto<List<ProductFeaturedResponseDto>> {
        return client.get("/producto/featured").body()
    }

    override suspend fun searchProducts(query: String, limit: Int?): ApiResponseDto<List<ProductFeaturedResponseDto>> {
        return client.get("/producto/search") {
            parameter("query", query)
            limit?.let { parameter("limit", it) }
        }.body()
    }
}