package com.feri.workshop

import android.app.Application
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WorkshopApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Firebase.analytics.setAnalyticsCollectionEnabled(true)
        Firebase.crashlytics.setCrashlyticsCollectionEnabled(true)
    }
}