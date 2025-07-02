package org.vi.be.kavivo.domain.comments.models

data class CommentModel (
    val id: String = "",
    val userName: String,
    val createdAt: Long,
    val comment: String,
    val groupId: String = "",
) {
    constructor() : this("", "", 0L, "", "")
}