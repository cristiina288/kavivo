package org.vi.be.kavivo.domain.users

import org.vi.be.kavivo.domain.users.models.UserModel


class RegisterUser (val authRepository: AuthRepository) {

    suspend operator fun invoke(user: UserModel): Result<UserModel> {
        return authRepository.register(user)
    }
}
