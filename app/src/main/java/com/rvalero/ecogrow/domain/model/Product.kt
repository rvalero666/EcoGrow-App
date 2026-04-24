package com.rvalero.ecogrow.domain.model

data class Product(
    val id: Long,
    val nombre: String,
    val productor: String,
    val precio: Double,
    val unidad: String,
    val descripcion: String = "",
    val imagenUrl: String? = null
)
