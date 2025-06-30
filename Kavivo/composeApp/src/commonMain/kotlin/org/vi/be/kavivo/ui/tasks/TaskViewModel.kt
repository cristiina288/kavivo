package org.vi.be.kavivo.ui.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.vi.be.kavivo.domain.tasks.AddTask
import org.vi.be.kavivo.domain.tasks.GetTasks
import org.vi.be.kavivo.domain.tasks.UpdateTask
import org.vi.be.kavivo.domain.tasks.models.TaskModel

class TaskViewModel(
    val getTasks: GetTasks,
    val updateTask: UpdateTask,
    val addTask: AddTask
) : ViewModel() {

    private val _tasks = MutableStateFlow<List<TaskModel>?>(null)
    val tasks: StateFlow<List<TaskModel>?> = _tasks

    private val _addTaskStatus = MutableStateFlow<Boolean>(false)
    val addTaskStatus: StateFlow<Boolean> = _addTaskStatus

    init {
        getAllTasks()
    }


    private fun getAllTasks() {
        viewModelScope.launch {
            val result: List<TaskModel> = withContext(Dispatchers.IO) {
                getTasks()
            }

            _tasks.value = result
        }
    }


    fun isChecked(task: TaskModel) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                updateTask(task)
            }
        }
    }


    fun saveTask(newTask: TaskModel) {
        viewModelScope.launch {
            viewModelScope.launch {
                val result: Boolean = withContext(Dispatchers.IO) {
                    addTask(newTask)
                }

                _addTaskStatus.value = result
            }
        }
    }
}

