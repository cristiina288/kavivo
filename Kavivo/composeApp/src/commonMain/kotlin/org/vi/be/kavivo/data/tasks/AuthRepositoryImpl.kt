package org.vi.be.kavivo.data.tasks

import org.vi.be.kavivo.domain.users.AuthRepository
import org.vi.be.kavivo.domain.users.models.UserModel

/*

class AuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.Default
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<UserModel> {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password)
            val user = firebaseAuth.currentUser
            Result.success(UserModel(user!!.uid, user.email ?: ""))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(email: String, password: String): Result<UserModel> {
        return try {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
            val user = firebaseAuth.currentUser
            Result.success(UserModel(user!!.uid, user.email ?: ""))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout() {
        firebaseAuth.signOut()
    }

    override fun isUserLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }
}*/
