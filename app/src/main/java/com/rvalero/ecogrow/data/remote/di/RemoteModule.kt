package com.rvalero.ecogrow.data.remote.di

import com.google.firebase.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import com.rvalero.ecogrow.data.remote.apiService.auth.AuthApiService
import com.rvalero.ecogrow.data.remote.apiService.auth.AuthApiServiceImpl
import com.rvalero.ecogrow.data.remote.apiService.producer.ProducerApiService
import com.rvalero.ecogrow.data.remote.apiService.producer.ProducerApiServiceImpl
import com.rvalero.ecogrow.data.remote.apiService.product.ProductApiService
import com.rvalero.ecogrow.data.remote.apiService.product.ProductApiServiceImpl
import com.rvalero.ecogrow.data.remote.storage.FirebaseImageUploader
import com.rvalero.ecogrow.data.remote.utils.TokenManager
import com.rvalero.ecogrow.data.remote.utils.provideHttpClient
import com.rvalero.ecogrow.data.remote.utils.provideImageHttpClient
import io.ktor.client.HttpClient
import org.koin.core.qualifier.named
import com.rvalero.ecogrow.data.repositoryImp.AuthRepositoryImpl
import com.rvalero.ecogrow.data.repositoryImp.ProducerRepositoryImpl
import com.rvalero.ecogrow.data.repositoryImp.ProductRepositoryImpl
import com.rvalero.ecogrow.domain.repository.AuthRepository
import com.rvalero.ecogrow.domain.repository.ImageUploader
import com.rvalero.ecogrow.domain.repository.ProducerRepository
import com.rvalero.ecogrow.domain.repository.ProductRepository
import com.rvalero.ecogrow.domain.useCase.product.GetFeaturedProductsUseCase
import com.rvalero.ecogrow.domain.useCase.product.GetProductDetailUseCase
import com.rvalero.ecogrow.domain.useCase.product.PublishProductUseCase
import com.rvalero.ecogrow.domain.useCase.product.SearchProductsUseCase
import com.rvalero.ecogrow.domain.useCase.producer.BecomeProducerUseCase
import com.rvalero.ecogrow.domain.useCase.producer.GetNearbyProducersUseCase
import com.rvalero.ecogrow.domain.useCase.auth.GetCurrentUserUseCase
import com.rvalero.ecogrow.domain.useCase.auth.GetUserNameUseCase
import com.rvalero.ecogrow.domain.useCase.auth.LoginUseCase
import com.rvalero.ecogrow.domain.useCase.auth.LogoutUseCase
import com.rvalero.ecogrow.domain.useCase.auth.RegisterUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val remoteModule = module {
    single { TokenManager(androidContext()) }
    single { provideHttpClient(get()) }
    single<HttpClient>(named("image")) { provideImageHttpClient() }
    single<AuthApiService> { AuthApiServiceImpl(get()) }
    single<ProducerApiService> { ProducerApiServiceImpl(get()) }
    single<ProductApiService> { ProductApiServiceImpl(get()) }
    single<FirebaseStorage> { Firebase.storage }
    single<ImageUploader> { FirebaseImageUploader(get()) }
}


val repositoryModule = module {
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    single<ProducerRepository> { ProducerRepositoryImpl(get(), get()) }
    single<ProductRepository> { ProductRepositoryImpl(get()) }
}


val useCaseModule = module {
    single { RegisterUseCase(get()) }
    single { LoginUseCase(get()) }
    single { LogoutUseCase(get()) }
    single { GetUserNameUseCase(get()) }
    single { GetCurrentUserUseCase(get()) }
    single { GetNearbyProducersUseCase(get()) }
    single { BecomeProducerUseCase(get()) }
    single { GetFeaturedProductsUseCase(get()) }
    single { SearchProductsUseCase(get()) }
    single { GetProductDetailUseCase(get()) }
    single { PublishProductUseCase(get(), get()) }
}
