package org.vi.be.kavivo.di

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await
import org.vi.be.kavivo.domain.tasks.TaskRepository
import org.vi.be.kavivo.domain.tasks.models.TaskModel

class FirebaseTaskRepository : TaskRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val tasksCollection = firestore.collection("tasks")

    override suspend fun getTasks(groupId: String): List<TaskModel> {
        val snapshot = tasksCollection
            .whereEqualTo("groupId", groupId)
            .get()
            .await()

        return snapshot.documents.mapNotNull { it.toObject<TaskModel>()?.copy(id = it.id) }
    }

    override suspend fun addTask(task: TaskModel): Boolean {
        try {
            tasksCollection.add(task).await()

            return true
        } catch (e: Exception) {
            return false
        }

    }

    override suspend fun updateTask(task: TaskModel) {
        tasksCollection.document(task.id).set(task).await()
    }

    override suspend fun deleteTask(taskId: String) {
        tasksCollection.document(taskId).delete().await()
    }
}
