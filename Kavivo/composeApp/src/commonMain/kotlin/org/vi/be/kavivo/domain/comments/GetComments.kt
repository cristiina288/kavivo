package org.vi.be.kavivo.domain.comments

import org.vi.be.kavivo.domain.comments.models.CommentModel


class GetComments (val commentsRepository: CommentsRepository) {

    suspend operator fun invoke(): List<CommentModel> {
        return commentsRepository.getComments()
    }
}
