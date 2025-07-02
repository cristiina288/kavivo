package org.vi.be.kavivo.domain.groups

import org.vi.be.kavivo.domain.groups.models.GroupModel

interface GroupsRepository {


    suspend fun getGroupsByIds(groupIds: List<String?>): List<GroupModel>

    suspend fun getGroupsById(groupId: String): Result<GroupModel?>

    suspend fun addGroup(group: GroupModel): Result<GroupModel>

    //suspend fun updateTask(task: TaskModel)

    //suspend fun deleteTask(taskId: String)
}