package com.rvalero.ecogrow.data.model.producer

import kotlinx.serialization.Serializable

@Serializable
data class BecomeProducerRequestDto(
    val nombreNegocio: String,
    val descripcion: String? = null,
    val localidad: String,
    val provincia: String
)
