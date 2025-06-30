package org.vi.be.kavivo.di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import org.vi.be.kavivo.domain.tasks.AddTask
import org.vi.be.kavivo.domain.tasks.GetTasks
import org.vi.be.kavivo.domain.tasks.UpdateTask

val domainModule = module {
    factoryOf(::GetTasks)
    factoryOf(::UpdateTask)
    factoryOf(::AddTask)
}