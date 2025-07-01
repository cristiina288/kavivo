package org.vi.be.kavivo.di

import org.vi.be.kavivo.ui.helpers.UserHelper

interface UserHelperProvider {
    fun provideUserHelper(): UserHelper
}