package com.rvalero.ecogrow.ui.becomeProducerScreen

data class BecomeProducerState(
    val nombreNegocio: String = "",
    val descripcion: String = "",
    val localidad: String = "",
    val provincia: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
) {
    val isFormValid: Boolean
        get() = nombreNegocio.isNotBlank() &&
                localidad.isNotBlank() &&
                provincia.isNotBlank()
}
