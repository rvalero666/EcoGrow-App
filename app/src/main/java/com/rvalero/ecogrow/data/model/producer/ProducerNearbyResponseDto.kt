package com.rvalero.ecogrow.data.model.producer

import kotlinx.serialization.Serializable

@Serializable
data class ProducerNearbyResponseDto(
    val id: Long,
    val nombreNegocio: String,
    val localidad: String,
    val verificado: Boolean,
    val distanciaKm: Double
)
