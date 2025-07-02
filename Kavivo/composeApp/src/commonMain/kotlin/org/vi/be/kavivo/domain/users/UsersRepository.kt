package org.vi.be.kavivo.domain.users

import org.vi.be.kavivo.domain.groups.models.GroupModel
import org.vi.be.kavivo.domain.users.models.UserModel


interface UsersRepository {

    suspend fun saveUser(user: UserModel, resultGroups: List<GroupModel>?)

    suspend fun addGroupIdToUser(group: GroupModel)

    suspend fun getUser(): UserModel?

    suspend fun clearUser()

    suspend fun saveGroupSelectedId(groupId: String)

    suspend fun getGroupSelectedId() : String?

    suspend fun saveDefaultGroupId(groupId: String)
}

