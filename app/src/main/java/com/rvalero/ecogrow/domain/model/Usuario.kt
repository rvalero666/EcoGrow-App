package com.rvalero.ecogrow.domain.model

data class Usuario(
    val nombre: String,
    val apellidos: String,
    val email: String,
    val password: String,
    val telefono: String? = null,
    val rol: String
)
