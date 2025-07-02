package org.vi.be.kavivo.domain.tasks

import org.vi.be.kavivo.domain.tasks.models.TaskModel


class GetTasks (val taskRepository: TaskRepository) {

    suspend operator fun invoke(groupId: String): List<TaskModel> {
        return taskRepository.getTasks(groupId)
    }
}
