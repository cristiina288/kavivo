package org.vi.be.kavivo.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.vi.be.kavivo.domain.tasks.AddTask
import org.vi.be.kavivo.domain.tasks.GetTasks
import org.vi.be.kavivo.domain.tasks.UpdateTask
import org.vi.be.kavivo.domain.tasks.models.TaskModel
import org.vi.be.kavivo.domain.users.UsersRepository
import org.vi.be.kavivo.domain.users.models.UserModel

class AppViewModel(val usersRepository: UsersRepository) : ViewModel() {

    private val _user = MutableStateFlow<UserModel?>(null)
    val user: StateFlow<UserModel?> = _user


    init {
        getUserInformation()
    }


    private fun getUserInformation() {
        viewModelScope.launch {

            val result: UserModel? = withContext(Dispatchers.IO) {
                usersRepository.getUser()
            }

            _user.value = result
        }
    }
}

