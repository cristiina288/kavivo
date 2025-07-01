package org.vi.be.kavivo.ui.helpers

import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.vi.be.kavivo.domain.users.models.UserModel

//var userInformation: UserModel? = null

class UserHelper(private val settings: Settings) {

    companion object {
        private const val USER_KEY = "user_info"
    }

    private val json = Json { ignoreUnknownKeys = true }

    fun saveUser(user: UserModel) {
        //userInformation = user

        val jsonString = json.encodeToString(user)
        settings[USER_KEY] = jsonString
    }

    fun getUser(): UserModel? {
        val jsonString = settings.getStringOrNull(USER_KEY) ?: return null

        var user = try {
            json.decodeFromString<UserModel>(jsonString)
        } catch (e: Exception) {
            null
        }

        //userInformation = user

        return user
    }

    fun clearUser() {
       // userInformation = null

        settings.remove(USER_KEY)
    }
}

interface UserHelperProvider {
    fun provideUserHelper(): UserHelper
}