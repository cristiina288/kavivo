package org.vi.be.kavivo

import androidx.compose.ui.window.ComposeUIViewController
import org.vi.be.kavivo.data.users.UsersRepositoryImpl
import org.vi.be.kavivo.di.IOSUserHelperProvider
import org.vi.be.kavivo.di.initKoin
import org.vi.be.kavivo.domain.users.UsersRepository
import org.vi.be.kavivo.ui.App

fun MainViewController() = ComposeUIViewController(configure = {
    initKoin()
    val userHelperProvider = IOSUserHelperProvider()
    val userHelper = userHelperProvider.provideUserHelper()
    val usersRepository: UsersRepository = UsersRepositoryImpl(userHelper)

}) { App() }