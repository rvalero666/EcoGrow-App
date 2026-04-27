package com.rvalero.ecogrow.data.repositoryImp

import com.rvalero.ecogrow.common.NetworkResult
import com.rvalero.ecogrow.common.safeApiCall
import com.rvalero.ecogrow.data.model.product.CreateProductRequestDto
import com.rvalero.ecogrow.data.model.product.ImagenRequestDto
import com.rvalero.ecogrow.data.remote.apiService.product.ProductApiService
import com.rvalero.ecogrow.domain.model.NewProduct
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
                    unidad = dto.unidad,
                    imagenUrl = dto.imagenUrl
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
                    unidad = dto.unidad,
                    imagenUrl = dto.imagenUrl
                )
            }
        }
    }

    override suspend fun getProductById(productId: Long): NetworkResult<ProductDetail> {
        return safeApiCall {
            val response = apiService.getProductById(productId)
            if (!response.success || response.data == null) throw Exception(response.message)
            response.data.toDomain()
        }
    }

    override suspend fun createProduct(newProduct: NewProduct): NetworkResult<Unit> {
        return safeApiCall {
            val response = apiService.createProduct(
                CreateProductRequestDto(
                    nombre = newProduct.nombre,
                    descripcion = newProduct.descripcion,
                    precio = newProduct.precio,
                    unidad = newProduct.unidad,
                    stock = newProduct.stock,
                    disponible = newProduct.disponible,
                    categoriaId = newProduct.categoriaId,
                    imagenes = newProduct.imagenes.map {
                        ImagenRequestDto(url = it.url, esPrincipal = it.esPrincipal)
                    }
                )
            )
            if (!response.success) throw Exception(response.message)
        }
    }
}

private fun com.rvalero.ecogrow.data.model.product.ProductDetailResponseDto.toDomain(): ProductDetail =
    ProductDetail(
        id = id,
        nombre = nombre,
        descripcion = descripcion ?: "",
        precio = precio,
        unidad = unidad,
        stock = stock,
        disponible = disponible,
        productorId = productor.id,
        productorNombre = productor.nombreNegocio,
        productorLocalidad = productor.localidad ?: "",
        productorVerificado = productor.verificado,
        categoria = categoria ?: "",
        imagenes = imagenes.map { it.url }
    )