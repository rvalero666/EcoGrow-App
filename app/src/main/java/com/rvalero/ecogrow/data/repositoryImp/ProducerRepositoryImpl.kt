package com.rvalero.ecogrow.data.repositoryImp

import com.rvalero.ecogrow.common.NetworkResult
import com.rvalero.ecogrow.common.safeApiCall
import com.rvalero.ecogrow.data.remote.apiService.producer.ProducerApiService
import com.rvalero.ecogrow.domain.model.Producer
import com.rvalero.ecogrow.domain.repository.ProducerRepository
import java.math.BigDecimal

class ProducerRepositoryImpl(
    private val apiService: ProducerApiService
) : ProducerRepository {

    override suspend fun getNearbyProducers(
        latitud: BigDecimal,
        longitud: BigDecimal,
        radioKm: Int?,
        limit: Int?
    ): NetworkResult<List<Producer>> {
        return safeApiCall {
            val response = apiService.getNearbyProducers(latitud, longitud, radioKm, limit)
            if (!response.success || response.data == null) throw Exception(response.message)
            response.data.map { dto ->
                Producer(
                    id = dto.id,
                    nombreNegocio = dto.nombreNegocio,
                    localidad = dto.localidad,
                    verificado = dto.verificado,
                    distanciaKm = dto.distanciaKm
                )
            }
        }
    }
}