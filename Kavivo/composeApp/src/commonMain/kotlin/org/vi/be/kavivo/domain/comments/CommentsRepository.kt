package org.vi.be.kavivo.domain.comments

import org.vi.be.kavivo.domain.comments.models.CommentModel

interface CommentsRepository {


    suspend fun getComments(groupId: String): List<CommentModel>

    suspend fun addComment(comment: CommentModel): Boolean

    suspend fun updateComment(comment: CommentModel)

    suspend fun deleteComment(commentId: String)
}