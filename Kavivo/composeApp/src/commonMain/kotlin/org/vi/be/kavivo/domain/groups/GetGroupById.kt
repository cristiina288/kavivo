package org.vi.be.kavivo.domain.groups

import org.vi.be.kavivo.domain.groups.models.GroupModel


class GetGroupById (val groupsRepository: GroupsRepository) {

    suspend operator fun invoke(groupId: String): Result<GroupModel?> {
        return groupsRepository.getGroupsById(groupId)
    }
}
