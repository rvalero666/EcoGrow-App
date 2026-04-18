package com.rvalero.ecogrow.ui.util.di

import com.rvalero.ecogrow.ui.activationScreen.ActivationViewModel
import com.rvalero.ecogrow.ui.homeScreen.HomeViewModel
import com.rvalero.ecogrow.ui.loginScreen.LoginViewModel
import com.rvalero.ecogrow.ui.registerScreen.RegisterViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {
    viewModel { LoginViewModel(get()) }
    viewModel { RegisterViewModel(get()) }
    viewModel { ActivationViewModel() }
    viewModel { HomeViewModel(get(), get()) }
}
