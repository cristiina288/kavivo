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

    private val _userStatus = MutableStateFlow<Boolean>(false)
    val userStatus: StateFlow<Boolean> = _userStatus

    private val _groupStatus = MutableStateFlow<Boolean>(false)
    val groupStatus: StateFlow<Boolean> = _groupStatus

    private val _groupJoinedStatus = MutableStateFlow<Boolean>(false)
    val groupJoinedStatus: StateFlow<Boolean> = _groupJoinedStatus

    private val _group = MutableStateFlow<GroupModel?>(null)
    val group: StateFlow<GroupModel?> = _group


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


    fun addGroup(name: String) {
        viewModelScope.launch {
            val newGroup = GroupModel (
                title = name,
                ownerId = user.value?.id ?: ""
            )

            val result: Result<GroupModel?> = withContext(Dispatchers.IO) {
                addGroup(newGroup)
            }

            _groupStatus.value = result.isSuccess

            result.onSuccess { group ->
                _group.value = group

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

                // Aquí gestionas el error
            }

        }
    }


    fun joinToGroup(groupId: String) {
        viewModelScope.launch {

            withContext(Dispatchers.IO) {
                addGroupToUser(groupId, user.value?.id ?: "")
            }

            var groupResult: Result<GroupModel?> = withContext(Dispatchers.IO) {
                getGroupById(groupId)
            }

            _groupJoinedStatus.value = groupResult.isSuccess

            groupResult.onSuccess { group ->
                _group.value = group

                if (group != null && user.value != null) {
                    if (user.value?.groupByDefault.isNullOrEmpty()) {
                        withContext(Dispatchers.IO) {
                            saveGroupByDefault(group.id, user.value!!)
                        }

                        usersRepository.saveGroupSelectedId(group.id)
                        usersRepository.saveDefaultGroupId(group.id)
                    }

                    usersRepository.addGroupIdToUser(group)
                }
            }.onFailure { exception ->
                _group.value = null

                // Aquí gestionas el error
            }
        }
    }

}