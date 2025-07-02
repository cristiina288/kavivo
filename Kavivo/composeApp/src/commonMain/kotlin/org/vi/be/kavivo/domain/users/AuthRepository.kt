package org.vi.be.kavivo.domain.users

import org.vi.be.kavivo.domain.users.models.UserModel

interface AuthRepository {

    suspend fun login(email: String, password: String): Result<UserModel?>

    suspend fun register(user: UserModel): Result<UserModel>

    suspend fun logout()

    fun isUserLoggedIn(): Boolean

    suspend fun getUserById(userId: String): UserModel?

    suspend fun addGroupToUser(groupId: String, userId: String)

    suspend fun saveGroupByDefault(groupId: String, user: UserModel)
}