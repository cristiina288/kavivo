package org.vi.be.kavivo.di

import org.koin.dsl.module
import org.vi.be.kavivo.domain.tasks.TaskRepository

actual val platformModule = module {
    single<TaskRepository> { FirebaseTaskRepository() }
}
