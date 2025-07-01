package org.vi.be.kavivo.di

import android.util.Log
import at.favre.lib.crypto.bcrypt.BCrypt
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await
import org.vi.be.kavivo.domain.tasks.TaskRepository
import org.vi.be.kavivo.domain.tasks.models.TaskModel
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
        try {
            val snapshot = usersCollection.document(userId).get().await()
            return snapshot.toObject<UserModel>()?.copy(id = snapshot.id)
        } catch (e: Exception) {
            return null
        }
    }
}
