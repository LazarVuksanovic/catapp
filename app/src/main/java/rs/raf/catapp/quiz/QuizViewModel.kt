package rs.raf.catapp.quiz

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import rs.raf.catapp.breeds.details.BreedDetailsState
import rs.raf.catapp.breeds.list.BreedListState
import rs.raf.catapp.breeds.repository.BreedRepository
import rs.raf.catapp.quiz.repository.PlayedQuizRepository
import rs.raf.catapp.users.db.asUserUIModel
import rs.raf.catapp.users.repository.UserRepository
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val playedQuizRepository: PlayedQuizRepository
) : ViewModel() {

    private val _state = MutableStateFlow(QuizUiState())
    val state = _state.asStateFlow()
    private fun setState(reducer: QuizUiState.() -> QuizUiState) = _state.getAndUpdate(reducer)

    init {
        observeGamesFlow()
    }

    private fun observeGamesFlow() {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                playedQuizRepository.observePlayedQuizFlow().distinctUntilChanged().collect {
                    setState { copy(previous = it) }
                }
            }
        }
    }

}