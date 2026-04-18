package com.rvalero.ecogrow.domain.useCase.producer

import com.rvalero.ecogrow.common.NetworkResult
import com.rvalero.ecogrow.domain.model.Producer
import com.rvalero.ecogrow.domain.repository.ProducerRepository
import java.math.BigDecimal

class GetNearbyProducersUseCase(
    private val producerRepository: ProducerRepository
) {

    suspend operator fun invoke(
        latitud: BigDecimal,
        longitud: BigDecimal,
        radioKm: Int? = null,
        limit: Int? = null
    ): NetworkResult<List<Producer>> {
        return producerRepository.getNearbyProducers(latitud, longitud, radioKm, limit)
    }
}