package com.rvalero.ecogrow.data.remote.apiService.product

import com.rvalero.ecogrow.data.model.ApiResponseDto
import com.rvalero.ecogrow.data.model.product.CreateProductRequestDto
import com.rvalero.ecogrow.data.model.product.ProductDetailResponseDto
import com.rvalero.ecogrow.data.model.product.ProductFeaturedResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class ProductApiServiceImpl(
    private val client: HttpClient
) : ProductApiService {

    override suspend fun getProductFeatured(): ApiResponseDto<List<ProductFeaturedResponseDto>> {
        return client.get("/producto/featured").body()
    }

    override suspend fun searchProducts(query: String, limit: Int?): ApiResponseDto<List<ProductFeaturedResponseDto>> {
        return client.get("/producto/search") {
            parameter("query", query)
            limit?.let { parameter("limit", it) }
        }.body()
    }

    override suspend fun getProductById(productId: Long): ApiResponseDto<ProductDetailResponseDto> {
        return client.get("/producto/$productId").body()
    }

    override suspend fun createProduct(
        body: CreateProductRequestDto
    ): ApiResponseDto<Unit> {
        return client.post("/producto") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }.body()
    }
}