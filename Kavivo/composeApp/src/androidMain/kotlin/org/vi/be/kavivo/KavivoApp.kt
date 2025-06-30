package org.vi.be.kavivo

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.vi.be.kavivo.di.initKoin

class KavivoApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidLogger()
            androidContext(this@KavivoApp)
        }
    }
}