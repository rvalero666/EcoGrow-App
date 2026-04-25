package com.rvalero.ecogrow.data.repositoryImp

import com.rvalero.ecogrow.common.NetworkResult
import com.rvalero.ecogrow.common.safeApiCall
import com.rvalero.ecogrow.data.model.producer.BecomeProducerRequestDto
import com.rvalero.ecogrow.data.remote.apiService.producer.ProducerApiService
import com.rvalero.ecogrow.data.remote.utils.TokenManager
import com.rvalero.ecogrow.domain.model.Producer
import com.rvalero.ecogrow.domain.repository.ProducerRepository
import java.math.BigDecimal

class ProducerRepositoryImpl(
    private val apiService: ProducerApiService,
    private val tokenManager: TokenManager
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
                    distanciaKm = dto.distanciaKm,
                    imagenUrl = dto.imagenUrl
                )
            }
        }
    }

    override suspend fun becomeProducer(
        nombreNegocio: String,
        descripcion: String?,
        localidad: String,
        provincia: String
    ): NetworkResult<Producer> {
        return safeApiCall {
            val response = apiService.becomeProducer(
                BecomeProducerRequestDto(
                    nombreNegocio = nombreNegocio,
                    descripcion = descripcion,
                    localidad = localidad,
                    provincia = provincia
                )
            )
            if (!response.success || response.data == null) throw Exception(response.message)
            tokenManager.updateUserRole(response.data.rol)
            Producer(
                id = response.data.id,
                nombreNegocio = response.data.nombreNegocio,
                localidad = response.data.localidad,
                verificado = response.data.verificado,
                distanciaKm = 0.0,
                imagenUrl = null
            )
        }
    }
}
