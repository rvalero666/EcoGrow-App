package com.rvalero.ecogrow.data.repositoryImp

import com.rvalero.ecogrow.data.model.auth.toDto
import com.rvalero.ecogrow.data.remote.apiService.AuthApiService
import com.rvalero.ecogrow.data.remote.utils.NetworkResult
import com.rvalero.ecogrow.data.remote.utils.safeApiCall
import com.rvalero.ecogrow.domain.model.Usuario
import com.rvalero.ecogrow.domain.repository.AuthRepository

class AuthRepositoryImp (
    private val apiService: AuthApiService
) : AuthRepository{


    override suspend fun register(registerRequest: Usuario): NetworkResult<String> {
        return safeApiCall { apiService.register(registerRequest.toDto()).message }
    }


}