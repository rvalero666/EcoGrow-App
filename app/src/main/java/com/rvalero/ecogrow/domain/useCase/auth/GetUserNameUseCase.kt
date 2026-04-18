package com.rvalero.ecogrow.domain.useCase.auth

import com.rvalero.ecogrow.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class GetUserNameUseCase(
    private val authRepository: AuthRepository
) {

    operator fun invoke(): Flow<String?> {
        return authRepository.getUserName()
    }
}
