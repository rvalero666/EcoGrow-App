package com.rvalero.ecogrow.data.remote.di

import com.rvalero.ecogrow.data.remote.apiService.AuthApiService
import com.rvalero.ecogrow.data.remote.apiService.AuthApiServiceImpl
import com.rvalero.ecogrow.data.remote.utils.TokenManager
import com.rvalero.ecogrow.data.remote.utils.provideHttpClient
import com.rvalero.ecogrow.data.repositoryImp.AuthRepositoryImpl
import com.rvalero.ecogrow.domain.repository.AuthRepository
import com.rvalero.ecogrow.domain.useCase.RegisterUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val remoteModule = module {
    single { TokenManager(androidContext()) }
    single { provideHttpClient(get()) }
    single<AuthApiService> { AuthApiServiceImpl(get()) }
}


val repositoryModule = module {
    single<AuthRepository> { AuthRepositoryImpl(get()) }
}


val useCaseModule = module {
    single { RegisterUseCase(get()) }
}
