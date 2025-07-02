package org.vi.be.kavivo.ui.login

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
import org.vi.be.kavivo.domain.users.LoginUser
import org.vi.be.kavivo.domain.users.RegisterUser
import org.vi.be.kavivo.domain.users.UsersRepository
import org.vi.be.kavivo.domain.users.models.UserModel


class LoginViewModel(
    val login: LoginUser,
    val register: RegisterUser,
    val usersRepository: UsersRepository,
    val getGroupsByIds: GetGroupsByIds
) : ViewModel() {

    private val _user = MutableStateFlow<UserModel?>(null)
    val user: StateFlow<UserModel?> = _user

    private val _userStatus = MutableStateFlow<Boolean>(false)
    val userStatus: StateFlow<Boolean> = _userStatus

    private val _onLoginError = MutableStateFlow<Boolean>(false)
    val onLoginError: StateFlow<Boolean> = _onLoginError


    fun registerUser(user: UserModel) {
        viewModelScope.launch {
            val result: Result<UserModel> = withContext(Dispatchers.IO) {
                register(user)
            }

            _userStatus.value = result.isSuccess

            result.onSuccess { user ->
                usersRepository.saveUser(user, emptyList())

                _user.value = user
            }.onFailure { exception ->
                // Aquí gestionas el error
            }

        }
    }


    fun loginUser(userEmail: String, userPassword: String) {
        viewModelScope.launch {
            val result: Result<UserModel?> = withContext(Dispatchers.IO) {
                login(userEmail, userPassword)
            }

            _userStatus.value = result.isSuccess

            result.onSuccess { user ->
                val resultGroups: List<GroupModel>? = withContext(Dispatchers.IO) {
                    getGroupsByIds(user?.groupIds ?: emptyList())
                }

                if (user != null) {
                    usersRepository.saveUser(user, resultGroups)
                }

                _onLoginError.value = false

                _user.value = user


            }.onFailure { exception ->
                _onLoginError.value = true

                // Aquí gestionas el error
            }
        }
    }

}