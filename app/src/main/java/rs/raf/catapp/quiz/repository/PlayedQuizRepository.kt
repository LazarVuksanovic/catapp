package rs.raf.catapp.quiz.repository

import rs.raf.catapp.db.AppDatabase
import rs.raf.catapp.quiz.db.PlayedQuiz
import rs.raf.catapp.quiz.db.PlayedQuizDao
import javax.inject.Inject

class PlayedQuizRepository @Inject constructor(
    private val database: AppDatabase,
) {
    suspend fun insertPlayedQuiz(game: PlayedQuiz){
        database.playedQuizDao().insert(game)
    }

    fun observePlayedQuizFlow() = database.playedQuizDao().observeGames()
}