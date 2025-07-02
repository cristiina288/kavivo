package org.vi.be.kavivo.di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import org.vi.be.kavivo.domain.comments.AddComment
import org.vi.be.kavivo.domain.comments.GetComments
import org.vi.be.kavivo.domain.groups.AddGroup
import org.vi.be.kavivo.domain.groups.GetGroupById
import org.vi.be.kavivo.domain.groups.GetGroupsByIds
import org.vi.be.kavivo.domain.tasks.AddTask
import org.vi.be.kavivo.domain.tasks.GetTasks
import org.vi.be.kavivo.domain.tasks.UpdateTask
import org.vi.be.kavivo.domain.users.AddGroupToUser
import org.vi.be.kavivo.domain.users.LoginUser
import org.vi.be.kavivo.domain.users.RegisterUser
import org.vi.be.kavivo.domain.users.SaveGroupByDefault

val domainModule = module {
    factoryOf(::GetTasks)
    factoryOf(::UpdateTask)
    factoryOf(::AddTask)

    factoryOf(::RegisterUser)
    factoryOf(::LoginUser)
    factoryOf(::AddGroupToUser)
    factoryOf(::SaveGroupByDefault)

    factoryOf(::GetComments)
    factoryOf(::AddComment)

    factoryOf(::AddGroup)
    factoryOf(::GetGroupsByIds)
    factoryOf(::GetGroupById)
}