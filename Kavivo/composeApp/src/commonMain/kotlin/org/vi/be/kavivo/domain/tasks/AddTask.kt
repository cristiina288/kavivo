package org.vi.be.kavivo.domain.tasks

import org.vi.be.kavivo.domain.tasks.models.TaskModel


class AddTask (val taskRepository: TaskRepository) {

    suspend operator fun invoke(task: TaskModel): Boolean {
        return taskRepository.addTask(task)
    }
}
