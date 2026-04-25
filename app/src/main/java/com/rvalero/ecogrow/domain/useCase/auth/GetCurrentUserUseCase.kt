package com.rvalero.ecogrow.domain.useCase.auth

import com.rvalero.ecogrow.domain.model.CurrentUser
import com.rvalero.ecogrow.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class GetCurrentUserUseCase(
    private val authRepository: AuthRepository
) {

    operator fun invoke(): Flow<CurrentUser> = authRepository.getCurrentUser()
}
