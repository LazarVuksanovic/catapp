package rs.raf.catapp.quiz

import rs.raf.catapp.quiz.db.PlayedQuiz

data class QuizUiState (
    val loading: Boolean = false,
    val previous: List<PlayedQuiz> = emptyList()
)