package com.rvalero.ecogrow.domain.model

data class ProductDetail(
    val id: Long,
    val nombre: String,
    val descripcion: String = "",
    val precio: Double,
    val unidad: String,
    val stock: Int = 0,
    val disponible: Boolean = true,
    val productorId: Long,
    val productorNombre: String,
    val productorLocalidad: String = "",
    val productorVerificado: Boolean = false,
    val categoria: String = "",
    val imagenes: List<String> = emptyList()
)
