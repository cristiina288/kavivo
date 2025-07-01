package org.vi.be.kavivo.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import org.vi.be.kavivo.data.users.UsersRepositoryImpl
import org.vi.be.kavivo.domain.tasks.TaskRepository
import org.vi.be.kavivo.domain.users.AuthRepository
import org.vi.be.kavivo.di.UserHelperProvider
import org.vi.be.kavivo.domain.comments.CommentsRepository
import org.vi.be.kavivo.domain.users.UsersRepository
import org.vi.be.kavivo.ui.helpers.UserHelper

actual val platformModule = module {
    single<TaskRepository> { FirebaseTaskRepository() }
    single<AuthRepository> { FirebaseAuthRepository() }
    single<UserHelper> { AndroidUserHelperProvider(androidContext()).provideUserHelper() }
    single<UsersRepository> { UsersRepositoryImpl(get()) }
    single<CommentsRepository> { FirebaseCommentsRepository() }
}
