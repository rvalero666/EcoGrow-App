package com.rvalero.ecogrow.domain.repository

import com.rvalero.ecogrow.common.NetworkResult
import com.rvalero.ecogrow.domain.model.Producer
import java.math.BigDecimal

interface ProducerRepository {

    suspend fun getNearbyProducers(
        latitud: BigDecimal,
        longitud: BigDecimal,
        radioKm: Int?,
        limit: Int?
    ): NetworkResult<List<Producer>>
}