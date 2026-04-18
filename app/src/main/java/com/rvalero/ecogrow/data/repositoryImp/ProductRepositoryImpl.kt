package com.rvalero.ecogrow.data.repositoryImp

import com.rvalero.ecogrow.common.NetworkResult
import com.rvalero.ecogrow.common.safeApiCall
import com.rvalero.ecogrow.data.remote.apiService.product.ProductApiService
import com.rvalero.ecogrow.domain.model.Product
import com.rvalero.ecogrow.domain.model.ProductDetail
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

    override suspend fun searchProducts(query: String, limit: Int?): NetworkResult<List<Product>> {
        return safeApiCall {
            val response = apiService.searchProducts(query, limit)
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

    override suspend fun getProductById(productId: Long): NetworkResult<ProductDetail> {
        return safeApiCall {
            val response = apiService.getProductById(productId)
            if (!response.success || response.data == null) throw Exception(response.message)
            val dto = response.data
            ProductDetail(
                id = dto.id,
                nombre = dto.nombre,
                descripcion = dto.descripcion ?: "",
                precio = dto.precio,
                unidad = dto.unidad,
                stock = dto.stock,
                disponible = dto.disponible,
                productorId = dto.productor.id,
                productorNombre = dto.productor.nombreNegocio,
                productorLocalidad = dto.productor.localidad ?: "",
                productorVerificado = dto.productor.verificado,
                categoria = dto.categoria ?: "",
                imagenes = dto.imagenes
            )
        }
    }
}