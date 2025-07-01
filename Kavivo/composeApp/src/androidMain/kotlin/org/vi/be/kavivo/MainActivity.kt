package org.vi.be.kavivo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.vi.be.kavivo.data.users.UsersRepositoryImpl
import org.vi.be.kavivo.di.AndroidUserHelperProvider
import org.vi.be.kavivo.domain.users.UsersRepository
import org.vi.be.kavivo.ui.App
import org.vi.be.kavivo.ui.helpers.UserHelper

class MainActivity : ComponentActivity() {

    //don't do it on ios all the helper
    private lateinit var userHelper: UserHelper
    lateinit var usersRepository: UsersRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        // Inicializas el UserSettings con el contexto Android
        val userHelperProvider = AndroidUserHelperProvider(this)
        userHelper = userHelperProvider.provideUserHelper()

        usersRepository = UsersRepositoryImpl(userHelper)

        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            App()
        }
    }
}

/*
@Preview
@Composable
fun AppAndroidPreview() {
    App(UsersRepository)
}*/
