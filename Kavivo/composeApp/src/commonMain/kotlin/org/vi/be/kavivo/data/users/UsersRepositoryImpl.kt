package org.vi.be.kavivo.data.users

import org.vi.be.kavivo.domain.groups.models.GroupModel
import org.vi.be.kavivo.domain.users.UsersRepository
import org.vi.be.kavivo.domain.users.models.UserModel
import org.vi.be.kavivo.ui.helpers.UserHelper

class UsersRepositoryImpl (private val userHelper: UserHelper) : UsersRepository {


    override suspend fun saveUser(user: UserModel, resultGroups: List<GroupModel>?) {
        userHelper.saveUser(user, resultGroups)
    }


    override suspend fun addGroupIdToUser(group: GroupModel) {
        userHelper.addGroupToUser(group)
    }


    override suspend fun getUser(): UserModel? {
        return userHelper.getUser()
    }


    override suspend fun clearUser() {
        userHelper.clearUser()
    }

    override suspend fun saveGroupSelectedId(groupId: String) {
        userHelper.saveGroupSelectedId(groupId)
    }

    override suspend fun getGroupSelectedId() : String? {
        return userHelper.getGroupSelectedId()
    }

    override suspend fun saveDefaultGroupId(groupId: String) {
        return userHelper.saveDefaultGroupId(groupId)
    }
}