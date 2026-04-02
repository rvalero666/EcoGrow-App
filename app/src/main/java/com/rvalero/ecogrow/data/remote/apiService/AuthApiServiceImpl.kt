package com.rvalero.ecogrow.data.remote.apiService

import com.rvalero.ecogrow.data.model.auth.MensajeResponseDto
import com.rvalero.ecogrow.data.model.auth.RegisterRequestDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class AuthApiServiceImpl(
    private val client: HttpClient
) : AuthApiService {

    override suspend fun register(request: RegisterRequestDto): MensajeResponseDto {
        return client.post("/auth/register") {
            setBody(request)
        }.body()
    }

}
