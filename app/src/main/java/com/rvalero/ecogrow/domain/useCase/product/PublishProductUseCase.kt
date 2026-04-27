package com.rvalero.ecogrow.domain.useCase.product

import com.rvalero.ecogrow.common.NetworkResult
import com.rvalero.ecogrow.domain.model.NewProduct
import com.rvalero.ecogrow.domain.model.NewProductDraft
import com.rvalero.ecogrow.domain.model.NewProductImage
import com.rvalero.ecogrow.domain.repository.ImageUploader
import com.rvalero.ecogrow.domain.repository.ProductRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class PublishProductUseCase(
    private val imageUploader: ImageUploader,
    private val productRepository: ProductRepository
) {

    suspend operator fun invoke(
        draft: NewProductDraft,
        photoUris: List<String>
    ): NetworkResult<Unit> {
        val uploadResults = coroutineScope {
            photoUris.map { uri ->
                async { imageUploader.upload(uri) }
            }.awaitAll()
        }

        uploadResults.firstOrNull { it is NetworkResult.Error }?.let {
            return it as NetworkResult.Error
        }

        val imagenes = uploadResults
            .filterIsInstance<NetworkResult.Success<String>>()
            .mapIndexed { index, result ->
                NewProductImage(url = result.data, esPrincipal = index == 0)
            }

        return productRepository.createProduct(
            NewProduct(
                nombre = draft.nombre,
                descripcion = draft.descripcion,
                categoriaId = draft.categoriaId,
                precio = draft.precio,
                unidad = draft.unidad,
                stock = draft.stock,
                disponible = draft.disponible,
                imagenes = imagenes
            )
        )
    }
}
