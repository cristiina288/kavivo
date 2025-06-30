package org.vi.be.kavivo.ui.login

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.vi.be.kavivo.domain.tasks.AddTask
import org.vi.be.kavivo.domain.tasks.GetTasks
import org.vi.be.kavivo.domain.tasks.UpdateTask
import org.vi.be.kavivo.domain.tasks.models.TaskModel


class LoginViewModel(
   // val login: UserLogin,
   // val register: UserRegister
) : ViewModel() {

    private val _tasks = MutableStateFlow<List<TaskModel>?>(null)
    val tasks: StateFlow<List<TaskModel>?> = _tasks


}