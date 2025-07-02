package org.vi.be.kavivo.domain.groups

import org.vi.be.kavivo.domain.groups.models.GroupModel
import org.vi.be.kavivo.domain.tasks.TaskRepository
import org.vi.be.kavivo.domain.tasks.models.TaskModel


class AddGroup (val groupsRepository: GroupsRepository) {

    suspend operator fun invoke(group: GroupModel): Result<GroupModel> {
        return groupsRepository.addGroup(group)
    }
}
