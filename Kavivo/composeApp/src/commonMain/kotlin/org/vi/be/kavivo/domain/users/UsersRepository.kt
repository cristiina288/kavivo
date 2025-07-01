package org.vi.be.kavivo.domain.users

import org.vi.be.kavivo.domain.users.models.UserModel


interface UsersRepository {

    suspend fun saveUser(user: UserModel)

    suspend fun getUser(): UserModel?

    suspend fun clearUser()
}

