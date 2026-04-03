package com.rvalero.ecogrow.ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable


sealed class Routes : NavKey{

    @Serializable
    data object RegisterRoute : Routes()

    @Serializable
    data class ActivationRoute(val email: String) : Routes()

}

