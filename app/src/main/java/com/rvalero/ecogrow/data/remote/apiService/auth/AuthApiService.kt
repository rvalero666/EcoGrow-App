package com.rvalero.ecogrow.data.remote.apiService.auth

import com.rvalero.ecogrow.data.model.ApiResponseDto
import com.rvalero.ecogrow.data.model.auth.AuthResponseDto
import com.rvalero.ecogrow.data.model.auth.LoginRequestDto
import com.rvalero.ecogrow.data.model.auth.RegisterRequestDto

interface AuthApiService {
    suspend fun register(request: RegisterRequestDto): ApiResponseDto<Unit>
    suspend fun login(request: LoginRequestDto): ApiResponseDto<AuthResponseDto>
}