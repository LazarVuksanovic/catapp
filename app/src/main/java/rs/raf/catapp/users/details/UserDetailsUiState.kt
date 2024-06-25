package rs.raf.catapp.users.details

import rs.raf.catapp.quiz.db.PlayedQuiz
import rs.raf.catapp.users.model.UserUiModel

data class UserDetailsUiState(
    val loading: Boolean = true,
    val user: UserUiModel? = null,
    val quizzes: List<PlayedQuiz> = emptyList(),
)
