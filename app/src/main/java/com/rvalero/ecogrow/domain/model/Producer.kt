package com.rvalero.ecogrow.domain.model

data class Producer(
    val id: Long,
    val nombreNegocio: String,
    val localidad: String,
    val verificado: Boolean,
    val distanciaKm: Double,
    val imagenUrl: String? = null
)
