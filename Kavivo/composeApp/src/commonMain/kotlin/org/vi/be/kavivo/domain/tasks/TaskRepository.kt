package org.vi.be.kavivo.domain.tasks

import org.vi.be.kavivo.domain.tasks.models.TaskModel

interface TaskRepository {
    suspend fun getTasks(): List<TaskModel>
    suspend fun addTask(task: TaskModel): Boolean
    suspend fun updateTask(task: TaskModel)
    suspend fun deleteTask(taskId: String)
}