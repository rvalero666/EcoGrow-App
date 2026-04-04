package com.rvalero.ecogrow.common

import com.google.firebase.crashlytics.FirebaseCrashlytics

object CrashlyticsLogger {

    fun logException(e: Exception) {
        FirebaseCrashlytics.getInstance().recordException(e)
    }

    fun log(message: String) {
        FirebaseCrashlytics.getInstance().log(message)
    }
}
