package org.vi.be.kavivo.di

import org.vi.be.kavivo.domain.comments.CommentsRepository
import org.vi.be.kavivo.domain.comments.models.CommentModel
import org.vi.be.kavivo.domain.tasks.TaskRepository
import org.vi.be.kavivo.domain.tasks.models.TaskModel

class FirebaseCommentsRepository : CommentsRepository {
    //private val firestore = FirebaseFirestore.getInstance()
    //private val tasksCollection = firestore.collection("tasks")


    override suspend fun getComments(): List<CommentModel> {
        TODO("Not yet implemented")
    }

    override suspend fun addComment(comment: CommentModel): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun updateComment(comment: CommentModel) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteComment(commentId: String) {
        TODO("Not yet implemented")
    }


}
