package com.rvalero.ecogrow.data.remote.apiService.producer

import com.rvalero.ecogrow.data.model.ApiResponseDto
import com.rvalero.ecogrow.data.model.producer.BecomeProducerRequestDto
import com.rvalero.ecogrow.data.model.producer.BecomeProducerResponseDto
import com.rvalero.ecogrow.data.model.producer.ProducerNearbyResponseDto
import java.math.BigDecimal

interface ProducerApiService {

    suspend fun getNearbyProducers(latitud: BigDecimal, longitud: BigDecimal, radioKm: Int?, limit: Int?): ApiResponseDto<List<ProducerNearbyResponseDto>>

    suspend fun becomeProducer(request: BecomeProducerRequestDto): ApiResponseDto<BecomeProducerResponseDto>
}