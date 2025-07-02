package org.vi.be.kavivo.domain.users

import org.vi.be.kavivo.domain.users.models.UserModel


class SaveGroupByDefault (val authRepository: AuthRepository) {

    suspend operator fun invoke(groupId: String, user: UserModel) {
        return authRepository.saveGroupByDefault(groupId, user)
    }
}
