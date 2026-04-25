package com.rvalero.ecogrow.data.repositoryImp

import com.rvalero.ecogrow.data.model.auth.LoginRequestDto
import com.rvalero.ecogrow.data.model.auth.toDto
import com.rvalero.ecogrow.data.remote.apiService.auth.AuthApiService
import com.rvalero.ecogrow.data.remote.utils.TokenManager
import com.rvalero.ecogrow.common.NetworkResult
import com.rvalero.ecogrow.common.safeApiCall
import com.rvalero.ecogrow.domain.model.CurrentUser
import com.rvalero.ecogrow.domain.model.UserRole
import com.rvalero.ecogrow.domain.model.Usuario
import com.rvalero.ecogrow.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class AuthRepositoryImpl(
    private val apiService: AuthApiService,
    private val tokenManager: TokenManager
) : AuthRepository {

    override fun getUserName(): Flow<String?> = tokenManager.getUserName()

    override fun getUserRole(): Flow<UserRole> = tokenManager.getUserRole()

    override fun getCurrentUser(): Flow<CurrentUser> =
        combine(tokenManager.getUserName(), tokenManager.getUserRole()) { nombre, rol ->
            CurrentUser(nombre = nombre, rol = rol)
        }

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
            tokenManager.saveTokens(
                accessToken = response.data.accessToken,
                refreshToken = response.data.refreshToken,
                nombre = response.data.nombre,
                rol = response.data.rol
            )
        }
    }
}