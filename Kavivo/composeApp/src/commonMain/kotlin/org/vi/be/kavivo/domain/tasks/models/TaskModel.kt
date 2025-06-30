package org.vi.be.kavivo.domain.tasks.models

data class TaskModel(
    val id: String = "",
    var title: String = "",
    val description: String?,
    var isDone: Boolean,
    val dueDate: Long? = null, //fecha limite
    val createdAt: Long = 0L,
    val updatedAt: Long? = null,
    val assignedUserId: String? = null
) {
    constructor() : this("", "", null, false, null, 0L, 0L, null)
}