package rs.raf.catapp.users.login

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import rs.raf.catapp.users.db.User
import rs.raf.catapp.users.db.asUserUIModel
import rs.raf.catapp.users.repository.UserRepository


@HiltViewModel
class UserViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(UserLoginUiState())
    val state = _state.asStateFlow()
    private fun setState(reducer: UserLoginUiState.() -> UserLoginUiState) = _state.update(reducer)
    private val events = MutableSharedFlow<UserLoginUiEvent>()

    init {
        fetchUsers()
        observeUsers()
        observeEvents()
    }

    fun publishEvent(event: UserLoginUiEvent) {
        viewModelScope.launch {
            events.emit(event)
        }
    }

    private fun observeEvents() {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                events.collect {
                    when (it) {
                        is UserLoginUiEvent.CreateUser -> {
                            userRepository.createUser(firstName = it.firstName, lastName = it.lastName,
                                mail = it.mail, username = it.username)
                        }

//                        is UserLoginUiEvent.UpdateUser -> {
//                            userRepository.updateUser(
//                                User(
//                                    id = it.id,
//                                    username = it.username,
//                                    mail = it.mail,
//                                    lastName = it.lastName,
//                                    firstName = it.firstName,
//                                    ranking = it.ranking
//                                )
//                            )
//                        }
                    }
                }
            }
        }
    }

    private fun fetchUsers(){
        viewModelScope.launch {
            setState { copy(loading = true) }

            try {
                val user = withContext(Dispatchers.IO) {
                    userRepository.fetchUsers()
                }
                Log.d("Print: ",user.toString())
                if(user != null) setState {copy(user = user.asUserUIModel())}

            }catch (error : Exception){
//                TO DO
            }
            setState { copy(loading = false) }
        }
    }

    private fun observeUsers(){
        viewModelScope.launch {
            userRepository.observeUser().distinctUntilChanged()
                .collect{
                    if(it != null) setState { copy(user = it.asUserUIModel()) }
                    else setState { copy(user = null) }
                }
        }
    }
}