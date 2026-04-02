package com.rvalero.ecogrow

import android.app.Application
import com.rvalero.ecogrow.data.remote.di.remoteModule
import com.rvalero.ecogrow.data.remote.di.repositoryModule
import com.rvalero.ecogrow.data.remote.di.useCaseModule
import com.rvalero.ecogrow.ui.di.uiModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class EcoGrowApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@EcoGrowApp)
            modules(remoteModule, uiModule, repositoryModule, useCaseModule)
        }
    }
}