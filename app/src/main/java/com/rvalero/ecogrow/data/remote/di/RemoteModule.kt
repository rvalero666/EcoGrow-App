package com.rvalero.ecogrow.data.remote.di

import com.rvalero.ecogrow.data.remote.apiService.auth.AuthApiService
import com.rvalero.ecogrow.data.remote.apiService.auth.AuthApiServiceImpl
import com.rvalero.ecogrow.data.remote.apiService.producer.ProducerApiService
import com.rvalero.ecogrow.data.remote.apiService.producer.ProducerApiServiceImpl
import com.rvalero.ecogrow.data.remote.apiService.product.ProductApiService
import com.rvalero.ecogrow.data.remote.apiService.product.ProductApiServiceImpl
import com.rvalero.ecogrow.data.remote.utils.TokenManager
import com.rvalero.ecogrow.data.remote.utils.provideHttpClient
import com.rvalero.ecogrow.data.repositoryImp.AuthRepositoryImpl
import com.rvalero.ecogrow.data.repositoryImp.ProducerRepositoryImpl
import com.rvalero.ecogrow.data.repositoryImp.ProductRepositoryImpl
import com.rvalero.ecogrow.domain.repository.AuthRepository
import com.rvalero.ecogrow.domain.repository.ProducerRepository
import com.rvalero.ecogrow.domain.repository.ProductRepository
import com.rvalero.ecogrow.domain.useCase.product.GetFeaturedProductsUseCase
import com.rvalero.ecogrow.domain.useCase.product.SearchProductsUseCase
import com.rvalero.ecogrow.domain.useCase.producer.GetNearbyProducersUseCase
import com.rvalero.ecogrow.domain.useCase.auth.GetUserNameUseCase
import com.rvalero.ecogrow.domain.useCase.auth.LoginUseCase
import com.rvalero.ecogrow.domain.useCase.auth.RegisterUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val remoteModule = module {
    single { TokenManager(androidContext()) }
    single { provideHttpClient(get()) }
    single<AuthApiService> { AuthApiServiceImpl(get()) }
    single<ProducerApiService> { ProducerApiServiceImpl(get()) }
    single<ProductApiService> { ProductApiServiceImpl(get()) }
}


val repositoryModule = module {
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    single<ProducerRepository> { ProducerRepositoryImpl(get()) }
    single<ProductRepository> { ProductRepositoryImpl(get()) }
}


val useCaseModule = module {
    single { RegisterUseCase(get()) }
    single { LoginUseCase(get()) }
    single { GetUserNameUseCase(get()) }
    single { GetNearbyProducersUseCase(get()) }
    single { GetFeaturedProductsUseCase(get()) }
    single { SearchProductsUseCase(get()) }
}
