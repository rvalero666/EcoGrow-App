package com.rvalero.ecogrow.ui.di

import com.rvalero.ecogrow.ui.activationScreen.ActivationViewModel
import com.rvalero.ecogrow.ui.registerScreen.RegisterViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {
    viewModel { RegisterViewModel(get()) }
    viewModel { ActivationViewModel() }
}
