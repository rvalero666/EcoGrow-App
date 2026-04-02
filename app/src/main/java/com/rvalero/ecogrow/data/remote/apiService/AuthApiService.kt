package com.rvalero.ecogrow.data.remote.apiService

import com.rvalero.ecogrow.data.model.auth.MensajeResponseDto
import com.rvalero.ecogrow.data.model.auth.RegisterRequestDto

interface AuthApiService {
    suspend fun register(request: RegisterRequestDto): MensajeResponseDto
}
