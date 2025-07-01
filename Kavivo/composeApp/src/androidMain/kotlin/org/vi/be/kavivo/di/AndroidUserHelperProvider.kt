package org.vi.be.kavivo.di

import android.content.Context
import com.russhwolf.settings.SharedPreferencesSettings
import org.vi.be.kavivo.ui.helpers.UserHelper
import org.vi.be.kavivo.di.UserHelperProvider

class AndroidUserHelperProvider(private val context: Context) : UserHelperProvider {


    override fun provideUserHelper(): UserHelper {
        val sharedPrefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val settings = SharedPreferencesSettings(sharedPrefs)
        return UserHelper(settings)
    }
}