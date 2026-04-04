package com.rvalero.ecogrow.data.repositoryImp

import com.rvalero.ecogrow.data.model.auth.LoginRequestDto
import com.rvalero.ecogrow.data.model.auth.toDto
import com.rvalero.ecogrow.data.remote.apiService.AuthApiService
import com.rvalero.ecogrow.data.remote.utils.TokenManager
import com.rvalero.ecogrow.common.NetworkResult
import com.rvalero.ecogrow.common.safeApiCall
import com.rvalero.ecogrow.domain.model.Usuario
import com.rvalero.ecogrow.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val apiService: AuthApiService,
    private val tokenManager: TokenManager
) : AuthRepository {

    override suspend fun register(registerRequest: Usuario): NetworkResult<String> {
        return safeApiCall {
            val response = apiService.register(registerRequest.toDto())
            if (!response.success) throw Exception(response.message)
            response.message
        }
    }

    override suspend fun login(email: String, password: String): NetworkResult<Unit> {
        return safeApiCall {
            val response = apiService.login(LoginRequestDto(email, password))
            if (!response.success || response.data == null) throw Exception(response.message)
            tokenManager.saveTokens(response.data.accessToken, response.data.refreshToken)
        }
    }
}