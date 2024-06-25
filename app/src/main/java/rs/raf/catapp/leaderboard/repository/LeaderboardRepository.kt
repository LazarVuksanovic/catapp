package rs.raf.catapp.leaderboard.repository

import rs.raf.catapp.leaderboard.api.LeaderboardApi
import rs.raf.catapp.leaderboard.model.QuizResult
import rs.raf.catapp.leaderboard.model.UserResult
import rs.raf.catapp.networking.retrofit
import javax.inject.Inject

class LeaderboardRepository @Inject constructor() {
    private val leaderBoardApi: LeaderboardApi = retrofit.create(LeaderboardApi::class.java)
    suspend fun getLeaderboard() : List<QuizResult> = leaderBoardApi.getLeaderboard()

    suspend fun postResult(quizResult: UserResult) =  leaderBoardApi.postResult(quizResult)
}