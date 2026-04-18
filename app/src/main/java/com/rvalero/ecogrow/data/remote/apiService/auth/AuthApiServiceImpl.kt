package com.rvalero.ecogrow.data.remote.apiService.auth

import com.rvalero.ecogrow.data.model.ApiResponseDto
import com.rvalero.ecogrow.data.model.auth.AuthResponseDto
import com.rvalero.ecogrow.data.model.auth.LoginRequestDto
import com.rvalero.ecogrow.data.model.auth.RegisterRequestDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class AuthApiServiceImpl(
    private val client: HttpClient
) : AuthApiService {

    override suspend fun register(request: RegisterRequestDto): ApiResponseDto<Unit> {
        return client.post("/auth/register") {
            setBody(request)
        }.body()
    }

    override suspend fun login(request: LoginRequestDto): ApiResponseDto<AuthResponseDto> {
        return client.post("/auth/login") {
            setBody(request)
        }.body()
    }
}