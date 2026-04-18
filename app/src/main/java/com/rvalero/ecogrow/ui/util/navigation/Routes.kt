package com.rvalero.ecogrow.ui.util.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable


sealed class Routes : NavKey {

    @Serializable
    data object LoginRoute : Routes()

    @Serializable
    data object RegisterRoute : Routes()

    @Serializable
    data class ActivationRoute(val email: String) : Routes()

    @Serializable
    data object HomeRoute : Routes()

    @Serializable
    data class ProductDetailRoute(val productId: Long) : Routes()
}

