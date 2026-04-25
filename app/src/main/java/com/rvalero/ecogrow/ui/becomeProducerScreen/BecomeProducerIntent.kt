package com.rvalero.ecogrow.ui.becomeProducerScreen

sealed interface BecomeProducerIntent {
    data class NombreNegocioChanged(val value: String) : BecomeProducerIntent
    data class DescripcionChanged(val value: String) : BecomeProducerIntent
    data class LocalidadChanged(val value: String) : BecomeProducerIntent
    data class ProvinciaChanged(val value: String) : BecomeProducerIntent
    data object Submit : BecomeProducerIntent
    data object NavigateBack : BecomeProducerIntent
}
