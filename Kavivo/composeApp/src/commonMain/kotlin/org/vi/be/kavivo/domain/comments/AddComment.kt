package org.vi.be.kavivo.domain.comments

import org.vi.be.kavivo.domain.comments.models.CommentModel
import org.vi.be.kavivo.domain.tasks.TaskRepository
import org.vi.be.kavivo.domain.tasks.models.TaskModel


class AddComment (val commentsRepository: CommentsRepository) {

    suspend operator fun invoke(comment: CommentModel): Boolean {
        return commentsRepository.addComment(comment)
    }
}
