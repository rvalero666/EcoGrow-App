package com.rvalero.ecogrow.ui.util.di

import com.rvalero.ecogrow.ui.activationScreen.ActivationViewModel
import com.rvalero.ecogrow.ui.becomeProducerScreen.BecomeProducerViewModel
import com.rvalero.ecogrow.ui.homeScreen.HomeViewModel
import com.rvalero.ecogrow.ui.loginScreen.LoginViewModel
import com.rvalero.ecogrow.ui.main.MainViewModel
import com.rvalero.ecogrow.ui.productDetailScreen.ProductDetailViewModel
import com.rvalero.ecogrow.ui.profileScreen.ProfileViewModel
import com.rvalero.ecogrow.ui.publishProductScreen.PublishProductViewModel
import com.rvalero.ecogrow.ui.registerScreen.RegisterViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {
    viewModel { LoginViewModel(get()) }
    viewModel { RegisterViewModel(get()) }
    viewModel { ActivationViewModel() }
    viewModel { HomeViewModel(get(), get(), get(), get()) }
    viewModel { (productId: Long) -> ProductDetailViewModel(productId, get()) }
    viewModel { MainViewModel(get()) }
    viewModel { ProfileViewModel(get(), get()) }
    viewModel { BecomeProducerViewModel(get()) }
    viewModel { PublishProductViewModel(get()) }
}
