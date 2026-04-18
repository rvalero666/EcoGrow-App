package com.rvalero.ecogrow.data.model.product

import kotlinx.serialization.Serializable

@Serializable
data class ProductFeaturedResponseDto(
    val id: Long,
    val nombre: String,
    val precio: Double,
    val unidad: String,
    val nombreNegocio: String
)
