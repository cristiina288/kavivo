package org.vi.be.kavivo.domain.users


class AddGroupToUser (val authRepository: AuthRepository) {

    suspend operator fun invoke(groupId: String, userId: String) {
        return authRepository.addGroupToUser(groupId, userId)
    }
}
