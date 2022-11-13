package com.interview.square

import android.app.Application
import com.interview.square.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class SquareApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            // Log Koin into Android logger
            androidLogger()
            // Reference Android context
            androidContext(this@SquareApplication)
            // Load modules
            modules(appModule)
        }
    }
}