package com.rvalero.ecogrow.domain.repository

import com.rvalero.ecogrow.common.NetworkResult
import com.rvalero.ecogrow.domain.model.Usuario

interface AuthRepository {

    suspend fun register(registerRequest: Usuario): NetworkResult<String>
}