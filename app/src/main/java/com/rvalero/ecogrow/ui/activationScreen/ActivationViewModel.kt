package com.rvalero.ecogrow.ui.activationScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rvalero.ecogrow.ui.util.UiEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ActivationViewModel : ViewModel() {

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun handleIntent(intent: ActivationIntent) {
        when (intent) {
            is ActivationIntent.NavigateToLogin -> {
                viewModelScope.launch { _uiEvent.send(UiEvent.NavigateBack) }
            }
        }
    }
}
