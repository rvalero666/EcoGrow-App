package com.rvalero.ecogrow.ui.util

interface UiEvent {
    data class ShowSnackbar(val message: String) : UiEvent
    data object NavigateBack : UiEvent

}
