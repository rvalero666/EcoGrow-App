package com.rvalero.ecogrow.domain.useCase.auth

import com.rvalero.ecogrow.common.NetworkResult
import com.rvalero.ecogrow.domain.repository.AuthRepository

class LogoutUseCase(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(): NetworkResult<Unit> {
        return authRepository.logout()
    }
}
