package org.vi.be.kavivo.di

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await
import org.vi.be.kavivo.domain.groups.GroupsRepository
import org.vi.be.kavivo.domain.groups.models.GroupModel
import org.vi.be.kavivo.domain.tasks.models.TaskModel

class FirebaseGroupRepository : GroupsRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val groupsCollection = firestore.collection("groups")


    override suspend fun getGroupsByIds(groupIds: List<String?>): List<GroupModel> {
        return try {
            // Si la lista está vacía, retornar lista vacía
            if (groupIds.isEmpty()) {
                return emptyList()
            }

            // Firestore tiene un límite de 10 elementos en consultas 'in'
            // Si tenemos más de 10 IDs, dividimos en chunks
            val chunks = groupIds.chunked(10)
            val allGroups = mutableListOf<GroupModel>()

            for (chunk in chunks) {
                val querySnapshot = groupsCollection
                    .whereIn("id", chunk)
                    .get()
                    .await()

                val groups = querySnapshot.documents.mapNotNull { document ->
                    try {
                        document.toObject<GroupModel>()
                    } catch (e: Exception) {
                        // Log del error si es necesario
                        println("Error al convertir documento ${document.id}: ${e.message}")
                        null
                    }
                }

                allGroups.addAll(groups)
            }

            allGroups.toList()

        } catch (e: Exception) {
            // Log del error si es necesario
            println("Error al obtener grupos por IDs: ${e.message}")
            emptyList()
        }
    }


    override suspend fun getGroupsById(groupId: String): Result<GroupModel?> {
        return try {
            val querySnapshot = groupsCollection
                .whereEqualTo("id", groupId)
                .get()
                .await()

            var group = querySnapshot.documents.firstNotNullOfOrNull {
                it.toObject<GroupModel>()?.copy(id = it.id)
            }

            Result.success(group)
        } catch (e: Exception) {
            // Log del error si es necesario
            println("Error al obtener grupo por Id: ${e.message}")

            Result.failure(e)
        }
    }


    override suspend fun addGroup(group: GroupModel): Result<GroupModel> {
        return try {
            val docRef = groupsCollection.add(group).await()
            val generatedId = docRef.id

            docRef.update("id", generatedId).await()

            group.id = generatedId

            Result.success(group)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
