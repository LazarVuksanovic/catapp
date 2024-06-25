package rs.raf.catapp.users.details

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import rs.raf.catapp.quiz.repository.PlayedQuizRepository
import rs.raf.catapp.users.db.User
import rs.raf.catapp.users.db.asUserUIModel
import rs.raf.catapp.users.login.UserLoginUiEvent
import rs.raf.catapp.users.login.UserLoginUiState
import rs.raf.catapp.users.repository.UserRepository
import javax.inject.Inject

@HiltViewModel
class UserDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository,
    private val quizRepository: PlayedQuizRepository
) : ViewModel() {

    private val _state = MutableStateFlow(UserDetailsUiState())
    val state = _state.asStateFlow()
    private fun setState(reducer: UserDetailsUiState.() -> UserDetailsUiState) = _state.update(reducer)
    private val events = MutableSharedFlow<UserDetailsUiEvent>()

    init {
        fetchUsers()
        observeUsers()
        observeEvents()
        observeGamesFlow()
    }

    fun publishEvent(event: UserDetailsUiEvent) {
        viewModelScope.launch {
            events.emit(event)
        }
    }

    private fun observeEvents() {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                events.collect {
                    when (it) {
                        is UserDetailsUiEvent.UpdateUser -> {
                            userRepository.updateUser(
                                User(
                                    id = it.id,
                                    username = it.username,
                                    mail = it.mail,
                                    lastName = it.lastName,
                                    firstName = it.firstName,
                                    ranking = it.ranking
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    private fun observeGamesFlow() {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                quizRepository.observePlayedQuizFlow().distinctUntilChanged().collect {
                    setState { copy(quizzes = it) }
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