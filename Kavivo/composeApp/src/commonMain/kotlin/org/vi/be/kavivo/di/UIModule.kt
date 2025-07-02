package org.vi.be.kavivo.di

import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import org.vi.be.kavivo.ui.AppViewModel
import org.vi.be.kavivo.ui.feed.FeedViewModel
import org.vi.be.kavivo.ui.groups.GroupsViewModel
import org.vi.be.kavivo.ui.login.LoginViewModel
import org.vi.be.kavivo.ui.tasks.TaskViewModel


val uiModule = module {
    viewModelOf(::TaskViewModel)
    viewModelOf(::LoginViewModel)

    viewModelOf(::AppViewModel)
    viewModelOf(::FeedViewModel)
    viewModelOf(::GroupsViewModel)
}