package org.vi.be.kavivo.ui.groups

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.vi.be.kavivo.domain.groups.AddGroup
import org.vi.be.kavivo.domain.groups.GetGroupById
import org.vi.be.kavivo.domain.groups.GetGroupsByIds
import org.vi.be.kavivo.domain.groups.models.GroupModel
import org.vi.be.kavivo.domain.users.AddGroupToUser
import org.vi.be.kavivo.domain.users.LoginUser
import org.vi.be.kavivo.domain.users.SaveGroupByDefault
import org.vi.be.kavivo.domain.users.UsersRepository
import org.vi.be.kavivo.domain.users.models.UserModel


class GroupsViewModel(
    val login: LoginUser,
    val addGroup: AddGroup,
    val usersRepository: UsersRepository,
    val addGroupToUser: AddGroupToUser,
    val getGroupById: GetGroupById,
    val saveGroupByDefault: SaveGroupByDefault
) : ViewModel() {

    private val _user = MutableStateFlow<UserModel?>(null)
    val user: StateFlow<UserModel?> = _user

    private val _groupStatus = MutableStateFlow<Boolean>(false)
    val groupStatus: StateFlow<Boolean> = _groupStatus

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _groupJoinedStatus = MutableStateFlow<Boolean>(false)
    val groupJoinedStatus: StateFlow<Boolean> = _groupJoinedStatus

    private val _group = MutableStateFlow<GroupModel?>(null)
    val group: StateFlow<GroupModel?> = _group

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
        }
    }


    fun editName(name: String) {
        viewModelScope.launch {
            _group.value?.copy(
                title = name
            )
//todo pending
            val result: Result<GroupModel?> = withContext(Dispatchers.IO) {
                editGroup(_group)
            }

            _groupStatus.value = result.isSuccess

            result.onSuccess { group ->
                _errorMessage.value = null

                if (group != null && user.value != null) {
                    if (user.value?.groupByDefault.isNullOrEmpty()) {
                        withContext(Dispatchers.IO) {
                            saveGroupByDefault(group.id, user.value!!)
                        }

                        usersRepository.saveGroupSelectedId(group.id)
                        usersRepository.saveDefaultGroupId(group.id)
                    }

                    withContext(Dispatchers.IO) {
                        addGroupToUser(group.id, user.value?.id ?: "")
                    }

                    usersRepository.addGroupIdToUser(group)
                }
            }.onFailure { exception ->
                _group.value = null
                _errorMessage.value = "Ha habido un error al crear el grupo. Vuelve a intentarlo."
                // Aquí gestionas el error
            }

        }
    }

    fun addErrorMessage(errorMessage: String?) {
        _errorMessage.value = errorMessage
    }


    fun getGroupForEdit (group: GroupModel) {
        _group.value = group
    }

}