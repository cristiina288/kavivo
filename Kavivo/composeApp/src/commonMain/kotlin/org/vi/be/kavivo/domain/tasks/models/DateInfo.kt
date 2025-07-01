package org.vi.be.kavivo.domain.tasks.models


// Data class para información de fecha
data class DateInfo(
    val dateMillis: Long,
    val title: String,
    val isOverdue: Boolean = false
)
