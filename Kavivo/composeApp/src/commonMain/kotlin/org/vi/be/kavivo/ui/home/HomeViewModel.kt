package org.vi.be.kavivo.ui.home

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

class HomeViewModel(
    val getTasks: GetTasks,
    val updateTask: UpdateTask,
    val addTask: AddTask,
    val usersRepository: UsersRepository
) : ViewModel() {

    private val _tasks = MutableStateFlow<List<TaskModel>?>(null)
    val tasks: StateFlow<List<TaskModel>?> = _tasks

    private val _addTaskStatus = MutableStateFlow<Boolean>(false)
    val addTaskStatus: StateFlow<Boolean> = _addTaskStatus

    private val _user = MutableStateFlow<UserModel?>(null)
    val user: StateFlow<UserModel?> = _user

    private val _groupSelectedId = MutableStateFlow<String>("")
    val groupSelectedId: StateFlow<String> = _groupSelectedId


    init {
        getUserInformation()
    }


    private fun getUserInformation() {
        viewModelScope.launch {

            val result: UserModel? = withContext(Dispatchers.IO) {
                usersRepository.getUser()
            }

            _user.value = result

            getGroupSelectedId()
        }
    }

    fun getAllTasks() {
        viewModelScope.launch {
            val result: List<TaskModel> = withContext(Dispatchers.IO) {
                getTasks(groupSelectedId.value)
            }

            _tasks.value = result
        }
    }


    fun isChecked(task: TaskModel) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                updateTask(task)
            }

            _tasks.update { tasks ->
                tasks?.map { currentTask ->
                    if (currentTask.id == task.id) {
                        currentTask.copy(isDone = task.isDone)
                    } else {
                        currentTask
                    }
                }
            }
        }
    }


    fun saveTask(newTask: TaskModel) {
        viewModelScope.launch {
            val result: Boolean = withContext(Dispatchers.IO) {
                addTask(newTask)
            }

            _addTaskStatus.value = result
        }
    }


    private fun getGroupSelectedId() {
        viewModelScope.launch {

            val result: String? = withContext(Dispatchers.IO) {
                usersRepository.getGroupSelectedId()
            }

            _groupSelectedId.value = result ?: _user.value?.groupByDefault ?: ""

            getAllTasks()
        }
    }
}

