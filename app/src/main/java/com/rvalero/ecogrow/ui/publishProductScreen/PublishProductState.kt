package com.rvalero.ecogrow.ui.publishProductScreen

data class PublishProductState(
    val nombre: String = "",
    val descripcion: String = "",
    val categoriaId: Int? = null,
    val precio: String = "",
    val unidad: String = "kg",
    val stock: String = "",
    val disponible: Boolean = true,
    val photos: List<String> = emptyList(),
    val isLoading: Boolean = false
) {
    val isFormValid: Boolean
        get() = nombre.isNotBlank() &&
                categoriaId != null &&
                precio.isNotBlank() &&
                stock.isNotBlank()
}

object PublishProductOptions {
    const val MAX_PHOTOS = 6
    val categorias = listOf(
        1 to "Frutas",
        2 to "Verduras",
        3 to "Lácteos",
        4 to "Cereales",
        5 to "Conservas"
    )
    val unidades = listOf("kg", "g", "l", "ml", "ud", "pack")
}
