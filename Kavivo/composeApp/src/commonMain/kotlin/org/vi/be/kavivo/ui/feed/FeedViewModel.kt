package org.vi.be.kavivo.ui.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.vi.be.kavivo.domain.comments.AddComment
import org.vi.be.kavivo.domain.comments.GetComments
import org.vi.be.kavivo.domain.comments.models.CommentModel
import org.vi.be.kavivo.domain.users.UsersRepository
import org.vi.be.kavivo.domain.users.models.UserModel

class FeedViewModel(
    val getComments: GetComments,
    val addComment: AddComment,
    val usersRepository: UsersRepository
) : ViewModel() {

    private val _comments = MutableStateFlow<List<CommentModel>?>(null)
    val comments: StateFlow<List<CommentModel>?> = _comments

    private val _addCommentStatus = MutableStateFlow<Boolean>(false)
    val addCommentStatus: StateFlow<Boolean> = _addCommentStatus

    private val _user = MutableStateFlow<UserModel?>(null)
    val user: StateFlow<UserModel?> = _user


    init {
        getAllComments()
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


    private fun getAllComments() {
        viewModelScope.launch {
            val result: List<CommentModel> = withContext(Dispatchers.IO) {
                getComments()
            }

            _comments.value = result
        }
    }
/*

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
*/

    fun saveComment(newComment: CommentModel) {
        viewModelScope.launch {
            viewModelScope.launch {
                val result: Boolean = withContext(Dispatchers.IO) {
                    addComment(newComment)
                }

                _addCommentStatus.value = result
            }
        }
    }
}

