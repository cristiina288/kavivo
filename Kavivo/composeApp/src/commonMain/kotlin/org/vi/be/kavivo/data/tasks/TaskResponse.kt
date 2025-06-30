package org.vi.be.kavivo.data.tasks

import kotlinx.serialization.Serializable


@Serializable
data class TaskResponse(
    val id: String,
    val title: String,
    val description: String?,
    val isDone: Boolean,
    val createdAt: Long
)