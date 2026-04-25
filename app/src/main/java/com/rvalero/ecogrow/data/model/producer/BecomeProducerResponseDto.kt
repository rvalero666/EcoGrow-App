package com.rvalero.ecogrow.data.model.producer

import kotlinx.serialization.Serializable

@Serializable
data class BecomeProducerResponseDto(
    val id: Long,
    val nombreNegocio: String,
    val descripcion: String? = null,
    val localidad: String,
    val provincia: String,
    val verificado: Boolean = false,
    val rol: String
)
