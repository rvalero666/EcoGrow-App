package com.rvalero.ecogrow

import android.app.Application
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.annotation.ExperimentalCoilApi
import coil3.network.ktor3.KtorNetworkFetcherFactory
import com.google.firebase.auth.FirebaseAuth
import com.rvalero.ecogrow.common.CrashlyticsLogger
import com.rvalero.ecogrow.data.remote.di.remoteModule
import com.rvalero.ecogrow.data.remote.di.repositoryModule
import com.rvalero.ecogrow.data.remote.di.useCaseModule
import com.rvalero.ecogrow.ui.util.di.uiModule
import coil3.util.DebugLogger
import io.ktor.client.HttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.qualifier.named

class EcoGrowApp : Application(), SingletonImageLoader.Factory {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@EcoGrowApp)
            modules(remoteModule, uiModule, repositoryModule, useCaseModule)
        }
        signInToFirebaseAnonymously()
    }

    private fun signInToFirebaseAnonymously() {
        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser == null) {
            auth.signInAnonymously()
                .addOnFailureListener { CrashlyticsLogger.logException(it) }
        }
    }

    override fun newImageLoader(context: PlatformContext): ImageLoader {
        val imageClient = GlobalContext.get().get<HttpClient>(qualifier = named("image"))
        return ImageLoader.Builder(context)
            .components {
                add(KtorNetworkFetcherFactory(httpClient = imageClient))
            }
            .logger(DebugLogger())
            .build()
    }
}