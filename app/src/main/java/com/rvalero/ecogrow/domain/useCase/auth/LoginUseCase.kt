package com.rvalero.ecogrow.domain.useCase.auth

import com.rvalero.ecogrow.common.NetworkResult
import com.rvalero.ecogrow.domain.repository.AuthRepository

class LoginUseCase(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(email: String, password: String): NetworkResult<Unit> {
        return authRepository.login(email, password)
    }
}