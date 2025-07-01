package org.vi.be.kavivo.di

import org.vi.be.kavivo.ui.helpers.UserHelper
import org.vi.be.kavivo.di.UserHelperProvider

class IOSUserHelperProvider  : UserHelperProvider {


    override fun provideUserHelper(): UserHelper {
        val settings = NativeSettings()
        return UserHelper(settings)
    }
}