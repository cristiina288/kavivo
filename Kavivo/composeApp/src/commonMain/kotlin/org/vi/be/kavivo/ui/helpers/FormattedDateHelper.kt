package org.vi.be.kavivo.ui.helpers

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime


// Función helper para formatear la fecha
fun formatDate(dateMillis: Long): String {
    val instant = Instant.fromEpochMilliseconds(dateMillis)
    val localDate = instant.toLocalDateTime(TimeZone.currentSystemDefault()).date
    return "${localDate.dayOfMonth}/${localDate.monthNumber}/${localDate.year}"
}


// Función para formatear títulos de fecha
fun formatDateTitle(date: LocalDate, isOverdue: Boolean = false): String {
    val months = listOf(
        "enero", "febrero", "marzo", "abril", "mayo", "junio",
        "julio", "agosto", "septiembre", "octubre", "noviembre", "diciembre"
    )

    val dayOfWeek = listOf(
        "lunes", "martes", "miércoles", "jueves", "viernes", "sábado", "domingo"
    )

    val monthName = months[date.monthNumber - 1].replaceFirstChar { it.uppercase() }
    val dayName = dayOfWeek[date.dayOfWeek.ordinal].replaceFirstChar { it.uppercase() }

    val prefix = if (isOverdue) "⚠️ " else ""

    return "$prefix$dayName, ${date.dayOfMonth} de $monthName"
}