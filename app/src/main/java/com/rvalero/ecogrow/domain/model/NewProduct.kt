package com.rvalero.ecogrow.domain.model

data class NewProduct(
    val nombre: String,
    val descripcion: String,
    val categoriaId: Int?,
    val precio: Double,
    val unidad: String,
    val stock: Int,
    val disponible: Boolean,
    val imagenes: List<NewProductImage>
)

data class NewProductDraft(
    val nombre: String,
    val descripcion: String,
    val categoriaId: Int?,
    val precio: Double,
    val unidad: String,
    val stock: Int,
    val disponible: Boolean
)

data class NewProductImage(
    val url: String,
    val esPrincipal: Boolean
)
