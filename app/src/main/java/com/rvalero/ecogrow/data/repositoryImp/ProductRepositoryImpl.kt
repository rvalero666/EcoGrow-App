package com.rvalero.ecogrow.data.repositoryImp

import com.rvalero.ecogrow.common.NetworkResult
import com.rvalero.ecogrow.common.safeApiCall
import com.rvalero.ecogrow.data.remote.apiService.product.ProductApiService
import com.rvalero.ecogrow.domain.model.Product
import com.rvalero.ecogrow.domain.repository.ProductRepository

class ProductRepositoryImpl(
    private val apiService: ProductApiService
) : ProductRepository {

    override suspend fun getFeaturedProducts(): NetworkResult<List<Product>> {
        return safeApiCall {
            val response = apiService.getProductFeatured()
            if (!response.success || response.data == null) throw Exception(response.message)
            response.data.map { dto ->
                Product(
                    id = dto.id,
                    nombre = dto.nombre,
                    productor = dto.nombreNegocio,
                    precio = dto.precio,
                    unidad = dto.unidad
                )
            }
        }
    }
}