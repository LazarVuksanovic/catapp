package rs.raf.catapp.leaderboard.model

import kotlinx.serialization.Serializable

@Serializable
data class QuizResult(
    val category: Int,
    val nickname: String,
    val result: Double,
    val createdAt: Long
)

fun QuizResult.asUiModel(): QuizResultUiModel{
    return QuizResultUiModel(
        nickname = this.nickname,
        result = this.result,
    )
}