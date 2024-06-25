package rs.raf.catapp.quiz

import rs.raf.catapp.breeds.list.model.BreedUiModel
import rs.raf.catapp.users.model.UserUiModel

data class QuestionUiState (
    val loading: Boolean= false,
    val breeds: List<BreedUiModel> = emptyList(),
    val questions: List<Question> = emptyList(),
    val currentQuestion: Int = 0,
    val rightAnswers: Int = 0,
    val finished: Boolean = false,
    val result: Double = 0.0,
    val user: UserUiModel? = null
)