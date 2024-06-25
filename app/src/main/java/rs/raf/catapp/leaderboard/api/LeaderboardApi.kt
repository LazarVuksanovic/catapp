package rs.raf.catapp.leaderboard.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import rs.raf.catapp.leaderboard.model.PostResultResponse
import rs.raf.catapp.leaderboard.model.QuizResult
import rs.raf.catapp.leaderboard.model.UserResult

interface LeaderboardApi {
    @GET("leaderboard?category=3\n")
    suspend fun getLeaderboard(): List<QuizResult>

    @POST("leaderboard")
    suspend fun postResult(
        @Body result: UserResult
    ): PostResultResponse
}