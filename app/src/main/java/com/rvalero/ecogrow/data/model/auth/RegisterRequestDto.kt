package com.rvalero.ecogrow.data.model.auth

import com.rvalero.ecogrow.domain.model.Usuario
import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequestDto(
    val nombre: String,
    val apellidos: String,
    val email: String,
    val password: String,
    val telefono: String? = null,
    val rol: String = "consumidor"
)

fun Usuario.toDto(): RegisterRequestDto = RegisterRequestDto(
    nombre = nombre,
    apellidos = apellidos,
    email = email,
    password = password,
    telefono = telefono,
    rol = rol
)
