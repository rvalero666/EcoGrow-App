package com.rvalero.ecogrow.ui.activationScreen

sealed interface ActivationIntent {
    data object NavigateToLogin : ActivationIntent
}
