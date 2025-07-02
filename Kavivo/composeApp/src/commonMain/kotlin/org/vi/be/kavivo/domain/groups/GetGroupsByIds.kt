package org.vi.be.kavivo.domain.groups

import org.vi.be.kavivo.domain.groups.models.GroupModel


class GetGroupsByIds (val groupsRepository: GroupsRepository) {

    suspend operator fun invoke(groupIds: List<String?>): List<GroupModel> {
        return groupsRepository.getGroupsByIds(groupIds)
    }
}
