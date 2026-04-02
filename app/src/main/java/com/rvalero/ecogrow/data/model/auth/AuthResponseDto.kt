package com.rvalero.ecogrow.data.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponseDto(
    val accessToken: String,
    val refreshToken: String
)
