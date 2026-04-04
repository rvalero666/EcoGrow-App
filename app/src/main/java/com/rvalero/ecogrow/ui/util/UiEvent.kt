package com.rvalero.ecogrow.ui.util

import com.rvalero.ecogrow.ui.util.navigation.Routes

 sealed interface UiEvent {
    data class ShowSnackbar(val message: String) : UiEvent
    data object NavigateBack : UiEvent
    data class NavigateTo(val route: Routes) : UiEvent
}
