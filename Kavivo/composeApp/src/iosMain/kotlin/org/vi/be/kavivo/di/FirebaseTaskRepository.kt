package org.vi.be.kavivo.di

import org.vi.be.kavivo.domain.tasks.TaskRepository
import org.vi.be.kavivo.domain.tasks.models.TaskModel

class FirebaseTaskRepository : TaskRepository {
    //private val firestore = FirebaseFirestore.getInstance()
    //private val tasksCollection = firestore.collection("tasks")

    override suspend fun getTasks(): List<TaskModel> {
        //val snapshot = tasksCollection.get().await()
       // return snapshot.documents.mapNotNull { it.toObject<TaskModel>()?.copy(id = it.id) }
        return emptyList()
    }

    override suspend fun addTask(task: TaskModel) {
       // tasksCollection.add(task).await()
    }

    override suspend fun updateTask(task: TaskModel) {
        //tasksCollection.document(task.id).set(task).await()
    }

    override suspend fun deleteTask(taskId: String) {
       // tasksCollection.document(taskId).delete().await()
    }
}
