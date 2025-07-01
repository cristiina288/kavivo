package org.vi.be.kavivo.di

import org.vi.be.kavivo.domain.users.AuthRepository
import org.vi.be.kavivo.domain.users.models.UserModel

class FirebaseAuthRepository : AuthRepository {
    //private val firestore = FirebaseFirestore.getInstance()
    //private val usersCollection = firestore.collection("users")


    override suspend fun login(
        email: String,
        password: String
    ): Result<UserModel> {
        TODO("Not yet implemented")
    }


    override suspend fun register(user: UserModel): Result<UserModel> {
        TODO("Not yet implemented")

    }


    override suspend fun logout() {
        TODO("Not yet implemented")
    }


    override fun isUserLoggedIn(): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getUserById(userId: String): UserModel? {
        TODO("Not yet implemented")
    }
}
