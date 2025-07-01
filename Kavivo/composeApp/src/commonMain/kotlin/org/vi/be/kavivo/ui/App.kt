package org.vi.be.kavivo.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import kavivo.composeapp.generated.resources.Res
import kavivo.composeapp.generated.resources.compose_multiplatform
import org.koin.compose.viewmodel.koinViewModel
import org.vi.be.kavivo.Greeting
import org.vi.be.kavivo.domain.users.UsersRepository
import org.vi.be.kavivo.ui.login.LoginScreen
import org.vi.be.kavivo.ui.tasks.TaskScreen
import org.vi.be.kavivo.ui.tasks.TaskViewModel
import org.vi.be.kavivo.ui.tasks.TasksScreen

@Composable
@Preview
fun App() {
    MaterialTheme {
        val appViewModel = koinViewModel<AppViewModel>()

        val user by appViewModel.user.collectAsState()

        if (user != null) {
            Navigator(screen = TaskScreen)
        } else {
            Navigator(screen = LoginScreen)
        }
    }
}