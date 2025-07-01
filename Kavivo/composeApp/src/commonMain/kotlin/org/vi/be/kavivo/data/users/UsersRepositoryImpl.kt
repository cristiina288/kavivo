package org.vi.be.kavivo.data.users

import org.vi.be.kavivo.domain.users.UsersRepository
import org.vi.be.kavivo.domain.users.models.UserModel
import org.vi.be.kavivo.ui.helpers.UserHelper

class UsersRepositoryImpl (private val userHelper: UserHelper) : UsersRepository {


    override suspend fun saveUser(user: UserModel) {
        userHelper.saveUser(user)
    }


    override suspend fun getUser(): UserModel? {
        return userHelper.getUser()
    }


    override suspend fun clearUser() {
        userHelper.clearUser()
    }
}