package rs.raf.catapp.users.login

import rs.raf.catapp.users.model.UserUiModel

data class UserLoginUiState(
    val loading: Boolean = true,
    val user: UserUiModel? = null,
)
