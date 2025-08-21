package org.vi.be.kavivo.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.vi.be.kavivo.domain.groups.GetGroupsByIds
import org.vi.be.kavivo.domain.groups.models.GroupModel
import org.vi.be.kavivo.domain.users.UsersRepository
import org.vi.be.kavivo.domain.users.models.UserModel

class AppViewModel(
    val usersRepository: UsersRepository,
    val getGroupsByIds: GetGroupsByIds,
) : ViewModel() {


    private val _user = MutableStateFlow<UserModel?>(null)
    val user: StateFlow<UserModel?> = _user

    private val _groupSelectedId = MutableStateFlow<String>("")
    val groupSelectedId: StateFlow<String> = _groupSelectedId


    init {
        getUserInformation()
    }


    fun getUserInformation() {
        viewModelScope.launch {

            val result: UserModel? = withContext(Dispatchers.IO) {
                usersRepository.getUser()
            }

            _user.value = result

            getGroupSelectedId()
        }
    }


    fun saveGroupSelectedId(groupId: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                usersRepository.saveGroupSelectedId(groupId)
            }

            _groupSelectedId.value = groupId
        }
    }


    private fun getGroupSelectedId() {
        viewModelScope.launch {

            val result: String? = withContext(Dispatchers.IO) {
                usersRepository.getGroupSelectedId()
            }

            _groupSelectedId.value = result ?: _user.value?.groupByDefault ?: ""
        }
    }


    fun logOut() {
        viewModelScope.launch {
            usersRepository.clearUser()

            _user.value = null
        }
    }
}

