package com.rvalero.ecogrow.data.model.product

import kotlinx.serialization.Serializable

@Serializable
data class ProductDetailResponseDto(
    val id: Long,
    val nombre: String,
    val descripcion: String? = null,
    val precio: Double,
    val unidad: String,
    val stock: Int = 0,
    val disponible: Boolean = true,
    val productor: ProductDetailProducerDto,
    val categoria: String? = null,
    val imagenes: List<String> = emptyList()
)

@Serializable
data class ProductDetailProducerDto(
    val id: Long,
    val nombreNegocio: String,
    val localidad: String? = null,
    val verificado: Boolean = false
)
