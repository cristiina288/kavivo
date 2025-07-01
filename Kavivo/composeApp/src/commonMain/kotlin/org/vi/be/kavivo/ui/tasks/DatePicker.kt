package org.vi.be.kavivo.ui.tasks

import androidx.compose.runtime.Composable

@Composable
expect fun DatePickerPersonalized(
    onDateSelected: (Long) -> Unit,
    onDismiss: () -> Unit,
    initialDate: Long? = null
)