package com.rvalero.ecogrow.data.remote.apiService.producer

import com.rvalero.ecogrow.data.model.ApiResponseDto
import com.rvalero.ecogrow.data.model.producer.ProducerNearbyResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import java.math.BigDecimal

class ProducerApiServiceImpl (
    private val client: HttpClient
): ProducerApiService {


    override suspend fun getNearbyProducers(
        latitud: BigDecimal,
        longitud: BigDecimal,
        radioKm: Int?,
        limit: Int?
    ): ApiResponseDto<List<ProducerNearbyResponseDto>> {
        return client.get("/productor/nearby"){
            parameter("latitud", latitud)
            parameter("longitud", longitud)
            radioKm?.let { parameter("radioKm", it)}
            limit?.let { parameter("limit", it)}
        }.body()
    }

}