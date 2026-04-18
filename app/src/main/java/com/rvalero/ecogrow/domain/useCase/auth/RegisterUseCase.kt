package com.rvalero.ecogrow.domain.useCase.auth

import com.rvalero.ecogrow.common.NetworkResult
import com.rvalero.ecogrow.domain.model.Usuario
import com.rvalero.ecogrow.domain.repository.AuthRepository

class RegisterUseCase(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(usuario: Usuario): NetworkResult<String> {
        return authRepository.register(usuario)
    }
}