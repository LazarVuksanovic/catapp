package rs.raf.catapp.quiz

import rs.raf.catapp.quiz.db.PlayedQuiz

sealed class QuestionUiEvent {
    data class NextQuestion(val userAnswer: Int, val timeLeft: Int) : QuestionUiEvent()
    data class PostQuiz(val playedQuiz: PlayedQuiz) : QuestionUiEvent()
    data class Publish(val username: String, val score: Double, val category: Int) : QuestionUiEvent()

}