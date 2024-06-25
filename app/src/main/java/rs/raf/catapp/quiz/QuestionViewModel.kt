package rs.raf.catapp.quiz

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import rs.raf.catapp.breeds.list.model.BreedUiModel
import rs.raf.catapp.breeds.repository.BreedRepository
import rs.raf.catapp.images.repository.ImageRepository
import rs.raf.catapp.leaderboard.model.UserResult
import rs.raf.catapp.leaderboard.repository.LeaderboardRepository
import rs.raf.catapp.navigation.dataId
import rs.raf.catapp.quiz.repository.PlayedQuizRepository
import rs.raf.catapp.users.db.asUserUIModel
import rs.raf.catapp.users.repository.UserRepository
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val breedsRepository: BreedRepository,
    private val userRepository: UserRepository,
    private val playedQuizRepository: PlayedQuizRepository,
    private val leaderboardRepository: LeaderboardRepository
) : ViewModel() {

    private val _state = MutableStateFlow(QuestionUiState())
    val state = _state.asStateFlow()
    private fun setState(reducer: QuestionUiState.() -> QuestionUiState) = _state.getAndUpdate(reducer)

    private val events = MutableSharedFlow<QuestionUiEvent>()

    fun publishEvent(event:QuestionUiEvent) {
        viewModelScope.launch {
            events.emit(event)
        }
    }

    init {
        observeEvents()
        fetchBreeds()
        observeBreeds()
        observeUser()
    }

    private fun observeEvents() {
        viewModelScope.launch {
            events.collect {
                when (it) {
                    is QuestionUiEvent.NextQuestion -> {
                        if (state.value.questions[state.value.currentQuestion].correctAnswer == it.userAnswer){
                            setState { copy(rightAnswers = rightAnswers+1) }
                        }
                        if ((state.value.currentQuestion == 19) or (it.timeLeft == 0)){
                            setState { copy(finished = true) }

                            val ubp = state.value.rightAnswers * 2.5 * (1 + (it.timeLeft + 120) / 300)
                            ubp.coerceAtMost(maximumValue = 100.0)
                            setState { copy(result = ubp) }
                        }
                        else{
                            setState { copy(currentQuestion = currentQuestion+1) }
                        }
                    }

                    is QuestionUiEvent.PostQuiz -> {
                        withContext(Dispatchers.IO){
                            playedQuizRepository.insertPlayedQuiz(it.playedQuiz)
                        }
                    }
                    is QuestionUiEvent.Publish -> {
                        withContext(Dispatchers.IO) {
                            val result = leaderboardRepository.postResult(
                                UserResult(
                                    category = it.category,
                                    nickname = it.username,
                                    result = it.score
                                )
                            )
                            val user = userRepository.getUser()
                            user.ranking = result.ranking
                            userRepository.updateUser(user)
                        }
                    }
                }
            }
        }
    }

    private fun observeBreeds() {
        viewModelScope.launch {
            breedsRepository.observeAllBreeds()
                .distinctUntilChanged()
                .collect {
                    setState {
                        copy(
                            breeds = it.map { it.asBreedUiModel() },
                        )
                    }
                }
        }
    }

    private fun fetchBreeds() {
        viewModelScope.launch {
            setState { copy(loading = true) }
            try {
                withContext(Dispatchers.IO){
                    breedsRepository.fetchAllBreeds()
                    makeQuiz()
                }
            } catch (error: IOException) {

            } finally {
                setState { copy(loading = false) }
            }
        }
    }

    private suspend fun makeQuiz() {
        val generatedQuestions: MutableList<Question> =  mutableListOf()

        val questionTexts = listOf(
            "Which cat is heavier on average?",
            "Which cat lives longer on average?",
        )

        repeat(20){
            var breed1: BreedUiModel
            var breed2: BreedUiModel
            do {
                breed1 =  state.value.breeds.random()
                breed2 = state.value.breeds.random()
            } while ((breed1.id == breed2.id) or (breed1.image == null) or (breed2.image == null))

            val correctAnswer: Int;
            val text = questionTexts.random()
            if (text == questionTexts[0]){
                correctAnswer = if (breed1.weight?.split(" - ")?.get(0)!!.toInt()
                    > breed2.weight?.split(" - ")?.get(0)!!.toInt()) 0 else 1
            }
            else {
                correctAnswer = if (breed1.lifeSpan.split(" - ")[0].toInt()
                    > breed2.lifeSpan.split(" - ")[0].toInt()) 0 else 1
            }

            generatedQuestions.add(Question(
                correctAnswer = correctAnswer,
                firstBreed = breed1,
                secondBreed = breed2,
                text = text
            ))
        }
        setState { copy(questions = generatedQuestions) }
    }

    private fun observeUser() {
        viewModelScope.launch {
            userRepository.observeUser()
                .distinctUntilChanged()
                .collect {
                    setState {
                        copy(user = it.asUserUIModel())
                    }
                }
        }
    }
}