package org.vi.be.kavivo.domain.users

import org.vi.be.kavivo.domain.users.models.UserModel


class LoginUser (val authRepository: AuthRepository) {

    suspend operator fun invoke(userEmail: String, userPassword: String): Result<UserModel?> {
        return authRepository.login(userEmail, userPassword)
    }
}
