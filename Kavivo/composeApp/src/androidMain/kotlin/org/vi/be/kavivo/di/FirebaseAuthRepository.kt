package org.vi.be.kavivo.di

import android.util.Log
import at.favre.lib.crypto.bcrypt.BCrypt
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import org.vi.be.kavivo.domain.users.AuthRepository
import org.vi.be.kavivo.domain.users.models.UserModel

class FirebaseAuthRepository : AuthRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val usersCollection = firestore.collection("users")


    override suspend fun login(
        email: String,
        password: String
    ): Result<UserModel?> {
        return try {
            val querySnapshot = usersCollection
                .whereEqualTo("email", email)
                .get()
                .await()

            val doc = querySnapshot.documents.firstOrNull()

            if (doc != null) {
                val user = doc.toObject(UserModel::class.java)?.copy(id = doc.id)

                if (user != null && verifyPasswordBcrypt(password, user.password)) {
                    Result.success(user)
                } else {
                    Result.failure(Exception("Credenciales incorrectas"))
                }
            } else {
                Result.failure(Exception("Usuario no encontrado"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    override suspend fun register(user: UserModel): Result<UserModel> {
        return try {
            val hashedPassword = hashPasswordBcrypt(user.password)
            val userWithHash = user.copy(password = hashedPassword)

            val docRef = usersCollection.add(userWithHash).await()
            val generatedId = docRef.id

            docRef.update("id", generatedId).await()

            userWithHash.id = generatedId

            Result.success(userWithHash)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    private fun hashPasswordBcrypt(password: String): String {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray())
    }


    private fun verifyPasswordBcrypt(password: String, hash: String): Boolean {
        try {
            val result = BCrypt.verifyer().verify(password.toCharArray(), hash)
            return result.verified

        } catch (e: Exception){
            var a = e.message
            Log.i("login", "loginerror: $a")
            return false
        }
    }


    override suspend fun logout() {
        TODO("Not yet implemented")
    }


    override fun isUserLoggedIn(): Boolean {
        TODO("Not yet implemented")
    }


    override suspend fun getUserById(userId: String): UserModel? {
        return try {
            val snapshot = usersCollection.document(userId).get().await()
            val data = snapshot.data ?: return null

            UserModel(
                id = snapshot.id,
                email = data["email"] as? String ?: "",
                name = data["name"] as? String ?: "",
                password = data["password"] as? String ?: "",
                groupIds = (data["groupIds"] as? List<*>)?.mapNotNull { it as? String } ?: emptyList()
            )
        } catch (e: Exception) {
            null
        }
    }


    override suspend fun addGroupToUser(groupId: String, userId: String) {
        try {
            var user = getUserById(userId)

            if (user?.groupByDefault.isNullOrEmpty()) {
                usersCollection.document(userId)
                    .update("groupByDefault", groupId)
                    .await()
            }

            usersCollection.document(userId)
                .update("groupIds", FieldValue.arrayUnion(groupId))
                .await()
        } catch (e: Exception) {
            // Puedes loguear o lanzar el error
            println("Error al añadir el grupo al usuario: ${e.message}")
        }
    }


    override suspend fun saveGroupByDefault(groupId: String, user: UserModel) {
        try {
            usersCollection.document(user.id)
                .update("groupByDefault", groupId)
                .await()
        } catch (e: Exception) {
            // Puedes loguear o lanzar el error
            println("Error al añadir el grupo por defecto al usuario: ${e.message}")
        }
    }
}
