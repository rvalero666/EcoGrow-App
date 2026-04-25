package com.rvalero.ecogrow.domain.useCase.producer

import com.rvalero.ecogrow.common.NetworkResult
import com.rvalero.ecogrow.domain.model.Producer
import com.rvalero.ecogrow.domain.repository.ProducerRepository

class BecomeProducerUseCase(
    private val producerRepository: ProducerRepository
) {

    suspend operator fun invoke(
        nombreNegocio: String,
        descripcion: String?,
        localidad: String,
        provincia: String
    ): NetworkResult<Producer> {
        return producerRepository.becomeProducer(
            nombreNegocio = nombreNegocio,
            descripcion = descripcion,
            localidad = localidad,
            provincia = provincia
        )
    }
}
