package org.vi.be.kavivo.domain.tasks

import org.vi.be.kavivo.domain.tasks.models.TaskModel


class UpdateTask (val taskRepository: TaskRepository) {

    suspend operator fun invoke(task: TaskModel) {
        taskRepository.updateTask(task)
    }
}
