package org.vi.be.kavivo.di

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await
import org.vi.be.kavivo.domain.comments.CommentsRepository
import org.vi.be.kavivo.domain.comments.models.CommentModel
import com.google.firebase.firestore.Query

class FirebaseCommentsRepository : CommentsRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val commentsCollection = firestore.collection("comments")


    override suspend fun getComments(groupId: String): List<CommentModel> {
        val snapshot = commentsCollection
            .whereEqualTo("groupId", groupId)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .get()
            .await()

        return snapshot.documents.mapNotNull { it.toObject<CommentModel>()?.copy(id = it.id) }
    }


    override suspend fun addComment(comment: CommentModel): Boolean {
        try {
            commentsCollection.add(comment).await()

            return true
        } catch (e: Exception) {
            return false
        }
    }


    override suspend fun updateComment(comment: CommentModel) {
        commentsCollection.document(comment.id).set(comment).await()
    }


    override suspend fun deleteComment(commentId: String) {
        commentsCollection.document(commentId).delete().await()
    }
}
