package com.rvalero.ecogrow.domain.repository

import com.rvalero.ecogrow.common.NetworkResult
import com.rvalero.ecogrow.domain.model.Usuario
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    suspend fun register(registerRequest: Usuario): NetworkResult<String>
    suspend fun login(email: String, password: String): NetworkResult<Unit>
    fun getUserName(): Flow<String?>
}