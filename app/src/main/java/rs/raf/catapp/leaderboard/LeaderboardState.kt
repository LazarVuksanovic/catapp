package rs.raf.catapp.leaderboard

import rs.raf.catapp.leaderboard.model.QuizResultUiModel

data class LeaderboardState(
    val leaderboard: List<QuizResultUiModel> = emptyList(),
    val loading: Boolean = false,
    val error: Exception? = null
)