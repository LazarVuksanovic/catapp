package rs.raf.catapp.leaderboard.model

import kotlinx.serialization.Serializable

@Serializable
data class UserResult(
    val nickname: String,
    val result: Double,
    val category: Int,
)
