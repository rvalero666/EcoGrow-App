package com.rvalero.ecogrow.ui.publishProductScreen

sealed interface PublishProductIntent {
    data class NombreChanged(val value: String) : PublishProductIntent
    data class DescripcionChanged(val value: String) : PublishProductIntent
    data class CategoriaIdChanged(val value: Int?) : PublishProductIntent
    data class PrecioChanged(val value: String) : PublishProductIntent
    data class UnidadChanged(val value: String) : PublishProductIntent
    data class StockChanged(val value: String) : PublishProductIntent
    data class DisponibleChanged(val value: Boolean) : PublishProductIntent
    data class PhotosPicked(val uris: List<String>) : PublishProductIntent
    data class PhotoRemoved(val uri: String) : PublishProductIntent
    data object Submit : PublishProductIntent
    data object NavigateBack : PublishProductIntent
}
