package org.vi.be.kavivo.ui.helpers

import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.vi.be.kavivo.domain.groups.models.GroupModel
import org.vi.be.kavivo.domain.users.models.UserModel

//var userInformation: UserModel? = null

class UserHelper(private val settings: Settings) {

    companion object {
        private const val USER_KEY = "user_info"
        private const val GROUPS_KEY = "groups_info"
        private const val GROUP_SELECTED_KEY = "group_selected_info"
    }


    private val json = Json { ignoreUnknownKeys = true }


    fun saveUser(user: UserModel, resultGroups: List<GroupModel>?) {
        //userInformation = user

        val jsonString = json.encodeToString(user)
        settings[USER_KEY] = jsonString

        val jsonStringGroups = json.encodeToString(resultGroups)
        settings[GROUPS_KEY] = jsonStringGroups
    }


    fun getUser(): UserModel? {
        val jsonString = settings.getStringOrNull(USER_KEY) ?: return null
        val jsonStringGroups = settings.getStringOrNull(GROUPS_KEY) ?: return null

        var user = try {
            json.decodeFromString<UserModel>(jsonString)
        } catch (e: Exception) {
            null
        }

        var groups = try {
            json.decodeFromString<List<GroupModel>>(jsonStringGroups)
        } catch (e: Exception) {
            null
        }

        user?.groups = groups

        //userInformation = user

        return user
    }


    fun clearUser() {
       // userInformation = null

        settings.remove(USER_KEY)
        settings.remove(GROUPS_KEY)
        settings.remove(GROUP_SELECTED_KEY)
    }


    fun addGroupToUser(group: GroupModel) {
        val user = getUser()

        val listOfGroups = user?.groups?.toMutableList() ?: mutableListOf()

        if (listOfGroups.none { it.id == group.id }) {
            listOfGroups.add(group)
        }

        val jsonStringGroups = json.encodeToString(listOfGroups)
        settings[GROUPS_KEY] = jsonStringGroups
    }


    fun saveGroupSelectedId(groupId: String) {
        val jsonString = json.encodeToString(groupId)
        settings[GROUP_SELECTED_KEY] = jsonString
    }


    fun getGroupSelectedId() : String? {
        val jsonString = settings.getStringOrNull(GROUP_SELECTED_KEY) ?: return null

        var groupSelectedId = try {
            json.decodeFromString<String>(jsonString)
        } catch (e: Exception) {
            null
        }

        return groupSelectedId
    }


    fun saveDefaultGroupId(groupId: String) {
        val user = getUser()

        user?.groupByDefault = groupId

        val jsonString = json.encodeToString(user)
        settings[USER_KEY] = jsonString
    }
}

interface UserHelperProvider {
    fun provideUserHelper(): UserHelper
}