package org.vi.be.kavivo.ui.helpers

import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import org.vi.be.kavivo.domain.tasks.models.DateInfo
import org.vi.be.kavivo.domain.tasks.models.TaskModel


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



// Función para agrupar tareas por fecha
fun groupTasksByDate(tasks: List<TaskModel>, onlyTodayTasks: Boolean): List<Pair<DateInfo, List<TaskModel>>> {
    val today = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .date

    // Filtrar solo tareas con fecha válida y agrupar por fecha
    val tasksWithDate = tasks
        .filter { it.dueDate != null && it.dueDate > 0 }
        .groupBy { task ->
            val taskDate = Instant.fromEpochMilliseconds(task.dueDate!!)
                .toLocalDateTime(TimeZone.currentSystemDefault())
                .date
            taskDate
        }

    // Tareas sin fecha (las ponemos al final)
    val tasksWithoutDate = tasks.filter { it.dueDate == null || it.dueDate <= 0 }

    // Crear lista ordenada de grupos
    val sortedGroups = mutableListOf<Pair<DateInfo, List<TaskModel>>>()

    // Ordenar fechas
    val sortedDates = if (onlyTodayTasks) {
        tasksWithDate.keys.filter { date -> date == today }.sorted()
    } else {
        tasksWithDate.keys.sorted()
    }

    sortedDates.forEach { date ->
        val dateMillis = date.atStartOfDayIn(TimeZone.currentSystemDefault()).toEpochMilliseconds()
        val allTasksForDate = tasksWithDate[date] ?: emptyList()

        // Filtrar tareas: si es anterior a hoy, solo mostrar las no completadas

        var tasksForDate = if (date < today) {
            allTasksForDate.filter { !it.isDone }
        } else {
            allTasksForDate
        }

        // Solo agregar el grupo si tiene tareas después del filtro
        if (tasksForDate.isNotEmpty()) {
            val dateInfo = when {
                date < today -> DateInfo(
                    dateMillis = dateMillis,
                    title = formatDateTitle(date, isOverdue = true),
                    isOverdue = true
                )

                date == today -> DateInfo(
                    dateMillis = dateMillis,
                    title = "Hoy",
                    isOverdue = false
                )

                date == today.plus(DatePeriod(days = 1)) -> DateInfo(
                    dateMillis = dateMillis,
                    title = "Mañana",
                    isOverdue = false
                )

                else -> DateInfo(
                    dateMillis = dateMillis,
                    title = formatDateTitle(date),
                    isOverdue = false
                )
            }

            sortedGroups.add(dateInfo to tasksForDate)
        }
    }

    // Agregar tareas sin fecha al final
    if (tasksWithoutDate.isNotEmpty()) {
        val noDateInfo = DateInfo(
            dateMillis = Long.MAX_VALUE,
            title = "Sin fecha",
            isOverdue = false
        )
        sortedGroups.add(noDateInfo to tasksWithoutDate)
    }

    return sortedGroups
}
