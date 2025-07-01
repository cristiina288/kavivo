package org.vi.be.kavivo.di

import org.koin.core.module.Module
import org.koin.dsl.module
import org.vi.be.kavivo.data.users.UsersRepositoryImpl
import org.vi.be.kavivo.domain.users.AuthRepository
import org.vi.be.kavivo.domain.users.UsersRepository
import org.vi.be.kavivo.ui.helpers.UserHelper


expect val platformModule: Module


val dataModule = module {

}
