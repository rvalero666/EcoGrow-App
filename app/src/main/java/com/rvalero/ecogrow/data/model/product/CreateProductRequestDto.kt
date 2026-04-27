package com.rvalero.ecogrow.data.model.product

import kotlinx.serialization.Serializable

@Serializable
data class CreateProductRequestDto(
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val unidad: String,
    val stock: Int,
    val disponible: Boolean,
    val categoriaId: Int?,
    val imagenes: List<ImagenRequestDto>
)

@Serializable
data class ImagenRequestDto(
    val url: String,
    val esPrincipal: Boolean
)
